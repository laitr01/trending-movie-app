package com.trachlai.trendingmovieapp.movie_listing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trachlai.trendingmovieapp.Config
import com.trachlai.trendingmovieapp.data.MovieRepository
import com.trachlai.trendingmovieapp.data.source.sharedpreferences.ApplicationPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.trachlai.trendingmovieapp.common.Result as Result

@HiltViewModel
class MovieListingViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val sharedPreferences: ApplicationPreference
) : ViewModel() {
    private val _recentQueries = MutableLiveData<List<String>>()
    val recentQueries: LiveData<List<String>> = _recentQueries

    private val _movieListingUiLiveData = MutableLiveData(MovieListingUiModel())
    val uiMovieListingLiveData: LiveData<MovieListingUiModel>
        get() = _movieListingUiLiveData

    init {
        loadFirst()
    }

    private fun fetchRecentQueries() {
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            _recentQueries.postValue(sharedPreferences.getRecentQueries().map { it })
        }
    }

    private fun saveSearchQuery(query: String) {
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            val queries = sharedPreferences.getRecentQueries().toMutableList().apply { add(query) }
            sharedPreferences.storeRecentQueries(queries.toSet())
            _recentQueries.postValue(queries)
        }
    }

    private fun fetchTrendingMovies(page: Int, forceUpdate: Boolean) {
        viewModelScope.launch {
            _movieListingUiLiveData.apply {
                if (value?.action() == Action.FirstLoad || value?.action() == Action.Reload || value?.action() == Action.Retry) {
                    postValue(value?.copy(uiState = UiState.Loading, viewType = ViewType.Trending))
                }
            }
            when (val result =
                movieRepository.getTrendingMovies(Config.API_VERSION, page, "day", forceUpdate)) {
                is Result.Success -> {
                    _movieListingUiLiveData.apply {
                        val list = value?.movieList()?.toMutableList()
                            ?.apply { addAll(result.data.movies) }
                        postValue(
                            value?.copy(
                                uiState = UiState.Success,
                                movies = list ?: mutableListOf(),
                                viewType = ViewType.Trending,
                                page = page,
                                hasNext = result.data.movies.isNotEmpty()
                            )
                        )
                    }
                }

                is Result.Error -> {
                    _movieListingUiLiveData.apply {
                        postValue(
                            value?.doUpdate(
                                uiState = UiState.Error, viewType = ViewType.Trending
                            )
                        )
                    }
                }
            }
        }
    }

    private fun requestSearchMovies(query: String, page: Int) {
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            _movieListingUiLiveData.apply {
                if (value?.action() == Action.FirstLoad || value?.action() == Action.Reload || value?.action() == Action.Retry) {
                    postValue(
                        value?.doUpdate(
                            uiState = UiState.Loading, viewType = ViewType.Search
                        )
                    )
                }
            }
            when (val result =
                movieRepository.requestSearchMovie(Config.API_VERSION, page, query)) {
                is Result.Success -> {
                    _movieListingUiLiveData.apply {
                        val list = value?.movieList()?.toMutableList()
                            ?.apply { addAll(result.data.movies) }

                        postValue(
                            value?.doUpdate(
                                uiState = UiState.Success,
                                movies = list ?: mutableListOf(),
                                viewType = ViewType.Search,
                                page = page,
                                hasNext = result.data.movies.isNotEmpty()
                            )
                        )
                    }
                }

                is Result.Error -> {
                    _movieListingUiLiveData.apply {
                        postValue(
                            value?.doUpdate(
                                uiState = UiState.Error, viewType = ViewType.Search
                            )
                        )
                    }
                }
            }
        }
    }

    private fun forceUpdate(currentTime: Long): Boolean {
        val previousTime = sharedPreferences.getCachedTime()
        val duration = Config.getCachedTrendingVideoDuration()
        val forceUpdate = forceUpdate(previousTime, currentTime, duration)
        if (forceUpdate) {
            sharedPreferences.storeCachedTime(currentTime)
        }
        return forceUpdate
    }

    private fun forceUpdate(previousTime: Long, currentTime: Long, duration: Long): Boolean {
        return currentTime - previousTime > duration
    }

    private fun fetchMovieListing(query: String?, forceUpdate: Boolean) {
        val type = if (query.isNullOrEmpty()) ViewType.Trending else ViewType.Search
        val currentType = _movieListingUiLiveData.value?.viewType()
        if (type != currentType) {
            _movieListingUiLiveData.value?.doUpdate(movies = mutableListOf(), page = 1)
        }
        val page = _movieListingUiLiveData.value?.page() ?: 0
        if (type == ViewType.Trending) {
            fetchTrendingMovies(page, forceUpdate)
        } else {
            saveSearchQuery(query!!)
            requestSearchMovies(query, page)
        }
    }

    fun reload(query: String?) {
        val page = 1
        _movieListingUiLiveData.value?.doUpdate(
            movies = mutableListOf(), action = Action.Reload, page = page
        )
        fetchMovieListing(query, forceUpdate(System.currentTimeMillis()))
        fetchRecentQueries()
    }

    private fun loadFirst() {
        val page = 1
        _movieListingUiLiveData.value?.doUpdate(
            movies = mutableListOf(), action = Action.FirstLoad, page = page
        )
        fetchMovieListing(null, forceUpdate(System.currentTimeMillis()))
        fetchRecentQueries()
    }

    fun loadNext(query: String?) {
        val model = _movieListingUiLiveData.value ?: return
        val isNext = model.hasNext()
        if (isNext) {
            model.doUpdate(hasNext = false, action = Action.LoadMore, page = model.page() + 1)
            fetchMovieListing(query, forceUpdate(System.currentTimeMillis()))
        }
    }
}
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
import com.trachlai.trendingmovieapp.utils.Result as Result

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

    private fun fetchTrendingMovies(page: Int) {
        viewModelScope.launch {
            _movieListingUiLiveData.apply {
                postValue(value?.copy(uiState = UiState.Loading, viewType = ViewType.Trending))
            }
            when (val result =
                movieRepository.getTrendingMovies(Config.API_VERSION, page, "day", forceUpdate())) {
                is Result.Success -> {
                    _movieListingUiLiveData.apply {
                        val list =
                            value?.movieList()?.toMutableList()
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
                                uiState = UiState.Error,
                                viewType = ViewType.Trending
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
                postValue(value?.doUpdate(uiState = UiState.Loading, viewType = ViewType.Search))
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
                                uiState = UiState.Error,
                                viewType = ViewType.Search
                            )
                        )
                    }
                }
            }
        }
    }

    private fun forceUpdate(): Boolean {
        val previousTime = sharedPreferences.getCachedTime()
        val currentTime = System.currentTimeMillis()
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

    private fun fetchMovieListing(query: String?, page: Int, action: Action) {
        val type = if (query.isNullOrEmpty()) ViewType.Trending else ViewType.Search
        val currentType = _movieListingUiLiveData.value?.viewType()
        var currentpage = page
        if (type != currentType) {
            currentpage = 1
            _movieListingUiLiveData.value?.doUpdate(movies = mutableListOf(), page = currentpage)
        }
        if (type == ViewType.Trending) {
            fetchTrendingMovies(currentpage)
        } else {
            saveSearchQuery(query!!)
            requestSearchMovies(query, currentpage)
        }
    }

    fun reload(query: String?) {
        fetchMovieListing(query, 1, Action.Reload)
        fetchRecentQueries()
    }

    private fun loadFirst() {
        _movieListingUiLiveData.value?.doUpdate(movies = mutableListOf())
        fetchMovieListing(null, 1, Action.FirstLoad)
        fetchRecentQueries()
    }

    fun loadNext(query: String?) {
        val model = _movieListingUiLiveData.value ?: return
        val isNext = model.hasNext()
        if (isNext) {
            model.doUpdate(hasNext = false)
            fetchMovieListing(query, model.page() + 1, Action.LoadMore)
        }
    }
}
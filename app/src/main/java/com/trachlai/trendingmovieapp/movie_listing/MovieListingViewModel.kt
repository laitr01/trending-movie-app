package com.trachlai.trendingmovieapp.movie_listing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trachlai.trendingmovieapp.Config
import com.trachlai.trendingmovieapp.data.MovieModel
import com.trachlai.trendingmovieapp.data.MovieRepository
import com.trachlai.trendingmovieapp.data.source.sharedpreferences.ApplicationPreference
import com.trachlai.trendingmovieapp.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _moviesListing = MutableLiveData<UIState<MovieModel>>()
    val moviesListing: LiveData<UIState<MovieModel>> = _moviesListing


    init {
        fetchTrendingMovies(1)
        fetchRecentQueries()
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
            _moviesListing.postValue(UIState.Loading)
            when (val result =
                movieRepository.getTrendingMovies(Config.API_VERSION, page, "day", forceUpdate())) {
                is Result.Success -> _moviesListing.postValue(UIState.Success(result.data))
                is Result.Error -> _moviesListing.postValue(UIState.Failure(result.exception))
            }
        }
    }

    private fun requestSearchMovies(query: String, page: Int) {
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            _moviesListing.postValue(UIState.Loading)
            when (val result =
                movieRepository.requestSearchMovie(Config.API_VERSION, page, query)) {
                is Result.Success -> _moviesListing.postValue(UIState.Success(result.data))
                is Result.Error -> _moviesListing.postValue(UIState.Failure(result.exception))
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

    fun searchFor(query: String?) {
        if (query.isNullOrEmpty()) {
            fetchTrendingMovies(1)
        } else {
            saveSearchQuery(query)
            requestSearchMovies(query, 1)
        }
    }
}
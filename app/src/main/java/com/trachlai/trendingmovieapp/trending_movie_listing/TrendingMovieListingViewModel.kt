package com.trachlai.trendingmovieapp.trending_movie_listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trachlai.trendingmovieapp.Config
import com.trachlai.trendingmovieapp.data.MovieRepository
import com.trachlai.trendingmovieapp.data.source.sharedpreferences.ApplicationPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingMovieListingViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val sharedPreferences: ApplicationPreference
): ViewModel() {
    init {
        fetchTrendingMovies()
    }

    private fun fetchTrendingMovies() {
        viewModelScope.launch {
            val result = movieRepository.getTrendingMovies(3, 1, "day", forceUpdate())
            result.toString()
        }
    }

    fun forceUpdate() : Boolean {
        val previousTime = sharedPreferences.getCachedTime()
        val currentTime = System.currentTimeMillis()
        val duration = Config.getCachedTrendingVideoDuration()
        val forceUpdate = forceUpdate(previousTime, currentTime, duration)
        if (forceUpdate) {
            sharedPreferences.storeCachedTime(currentTime)
        }
        return forceUpdate
    }

    fun forceUpdate(previousTime: Long, currentTime: Long,duration: Long) : Boolean {
        return currentTime - previousTime > duration
    }
}
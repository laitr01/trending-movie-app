package com.trachlai.trendingmovieapp.trending_movie_listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trachlai.trendingmovieapp.data.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingMovieListingViewModel @Inject constructor(
    private val movieRepository: MovieRepository
): ViewModel() {
    init {
        fetchTrendingMovies()
    }

    private fun fetchTrendingMovies() {
        viewModelScope.launch {
            val result = movieRepository.getTrendingMovies(3, 1, "day", false)
            result.toString()
        }
    }
}
package com.trachlai.trendingmovieapp.movie_detail

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trachlai.trendingmovieapp.Config
import com.trachlai.trendingmovieapp.common.Result
import com.trachlai.trendingmovieapp.common.UIState
import com.trachlai.trendingmovieapp.data.MovieRepository
import com.trachlai.trendingmovieapp.data.source.MovieDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _movieDetailLiveData = MutableLiveData<UIState<MovieDetail>>()
    val movieDetailLiveData: LiveData<UIState<MovieDetail>>
        get() = _movieDetailLiveData

    fun fetchMovieDetail(intent: Intent) {
        viewModelScope.launch {
            _movieDetailLiveData.postValue(UIState.Loading)
            val movieId = intent.getLongExtra(MOVIE_ID, -1)
            when (val result =
                movieRepository.fetchTrendingMovieDetail(Config.API_VERSION, movieId)) {
                is Result.Success -> {
                    _movieDetailLiveData.postValue(UIState.Success(result.data))
                }

                is Result.Error -> {
                    _movieDetailLiveData.postValue(UIState.Failure(result.exception))
                }
            }
        }
    }

    companion object {
        const val MOVIE_ID = "MOVIE_ID"
    }
}
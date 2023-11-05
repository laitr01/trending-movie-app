package com.trachlai.trendingmovieapp.data
import com.trachlai.trendingmovieapp.utils.Result as Result

interface MovieRepository {
    suspend fun getTrendingMovies(
        version: Int,
        page: Int,
        windowTime: String,
        forceUpdate: Boolean
    ) : Result<List<Movie>>
    suspend fun requestSearchMovie(version: Int, page: Int, query: String): Result<List<Movie>>
    suspend fun fetchTrendingMovieDetail(version: Int, movieId: Long): Result<Movie>

    suspend fun refresh(version: Int, page: Int, windowTime: String): Exception?
}
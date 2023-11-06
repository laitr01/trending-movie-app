package com.trachlai.shared_test.data

import com.trachlai.trendingmovieapp.data.Movie
import com.trachlai.trendingmovieapp.data.MovieModel
import com.trachlai.trendingmovieapp.data.MovieRepository
import com.trachlai.trendingmovieapp.utils.Result

class FakeTaskRepository: MovieRepository {
    override suspend fun getTrendingMovies(
        version: Int,
        page: Int,
        windowTime: String,
        forceUpdate: Boolean
    ): Result<MovieModel> {
        TODO("Not yet implemented")
    }

    override suspend fun requestSearchMovie(
        version: Int,
        page: Int,
        query: String
    ): Result<MovieModel> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchTrendingMovieDetail(version: Int, movieId: Long): Result<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun refresh(version: Int, page: Int, windowTime: String): Exception? {
        TODO("Not yet implemented")
    }
}
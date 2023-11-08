package com.trachlai.shared_test.data.source

import com.trachlai.trendingmovieapp.data.source.remote.MovieRemoteDataSource
import com.trachlai.trendingmovieapp.data.source.remote.RemoteMovie
import com.trachlai.trendingmovieapp.data.source.remote.RemoteMovieResponse
import com.trachlai.trendingmovieapp.common.Result
import com.trachlai.trendingmovieapp.data.source.remote.RemoteMovieDetail
import java.lang.Exception

class FakeNetworkDataSource(private val movieResponse: RemoteMovieResponse?): MovieRemoteDataSource {
    override suspend fun searchMovies(
        version: Int,
        query: String,
        page: Int
    ): Result<RemoteMovieResponse> {
        return if (movieResponse != null) {
            Result.Success(movieResponse)
        }else{
            Result.Error(Exception("Movie response is empty"))
        }
    }

    override suspend fun fetchTrendingMovies(
        version: Int,
        time: String,
        page: Int
    ): Result<RemoteMovieResponse> {
        return if (movieResponse != null) {
            Result.Success(movieResponse)
        }else{
            Result.Error(Exception("Movie response is empty"))
        }
    }

    override suspend fun fetchTrendingMovieDetail(
        version: Int,
        movieId: Long
    ): Result<RemoteMovieDetail> {
        TODO("Not yet implemented")
    }


}
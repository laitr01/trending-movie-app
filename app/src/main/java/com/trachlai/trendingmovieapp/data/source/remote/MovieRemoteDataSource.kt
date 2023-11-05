package com.trachlai.trendingmovieapp.data.source.remote

import com.trachlai.trendingmovieapp.utils.Result as Result
interface MovieRemoteDataSource {
    suspend fun searchMovies(version: Int, query: String, page: Int): Result<RemoteMovieResponse>
    suspend fun fetchTrendingMovies(version: Int, time: String, page: Int): Result<RemoteMovieResponse>
}
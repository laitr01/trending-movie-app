package com.trachlai.trendingmovieapp.data.source.remote

interface MovieRemoteDataSource {
    suspend fun searchMovies(version: Int, query: String, page: Int) : List<RemoteMovie>
    suspend fun fetchTrendingMovies(version: Int, time: String, page: Int) : List<RemoteMovie>
}
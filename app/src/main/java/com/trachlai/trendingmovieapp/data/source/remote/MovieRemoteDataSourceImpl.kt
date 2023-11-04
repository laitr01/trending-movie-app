package com.trachlai.trendingmovieapp.data.source.remote

import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(): MovieRemoteDataSource {
    override suspend fun searchMovies(version: Int, query: String, page: Int): List<RemoteMovie> {

    }

    override suspend fun fetchTrendingMovies(version: Int, time: String, page: Int): List<RemoteMovie> {

    }
}
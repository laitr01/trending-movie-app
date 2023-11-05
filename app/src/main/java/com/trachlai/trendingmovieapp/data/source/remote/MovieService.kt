package com.trachlai.trendingmovieapp.data.source.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("{version}/search/movie")
    suspend fun searchMovies(
        @Path("version") version: Int,
        @Query("query") query: String,
        @Query("page") page: Int
    ) : Response<RemoteMovieResponse>

    @GET("{version}/movie/{movie_id}")
    suspend fun requestMovieDetail(
        @Path("version") version: Int,
        @Path("movie_id") movieId: Long
    ) : Response<RemoteMovie>
}
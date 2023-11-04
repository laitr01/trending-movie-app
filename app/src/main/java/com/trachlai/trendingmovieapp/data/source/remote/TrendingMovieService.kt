package com.trachlai.trendingmovieapp.data.source.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrendingMovieService {
    @GET("{version}/trending/movie/{time_window}")
    suspend fun fetchTrendingMovies(
        @Path("version") version : Int,
        @Path("time_window") time: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ) : Response<RemoteMovie>
}
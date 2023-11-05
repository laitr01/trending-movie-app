package com.trachlai.trendingmovieapp.data.source.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchMovieService {
    @GET("{version}/search/movie")
    suspend fun searchMovies(
        @Path("version") version: Int,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ) : Response<RemoteMovieResponse>
}
package com.trachlai.trendingmovieapp.data.source.remote

import com.trachlai.trendingmovieapp.Config
import com.trachlai.trendingmovieapp.utils.safeApiCall
import java.io.IOException
import javax.inject.Inject
import com.trachlai.trendingmovieapp.utils.Result as Result
class MovieRemoteDataSourceImpl @Inject constructor(
    private val searchService: SearchMovieService,
    private val trendingService: TrendingMovieService
): MovieRemoteDataSource {
    override suspend fun searchMovies(version: Int, query: String, page: Int): Result<RemoteMovieResponse> = safeApiCall (
            call = {
                   val response = searchService.searchMovies(version, query, page, Config.API_KEY)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.Success(body)
                    }
                }
                Result.Error(IOException("Error searching movie ${response.code()} ${response.message()}"))
            },
            errorMessage = "Error fetching search movie keyword $query"
         )


    override suspend fun fetchTrendingMovies(version: Int, time: String, page: Int): Result<RemoteMovieResponse> = safeApiCall (
        call = {
            val response = trendingService.fetchTrendingMovies(version,time, page, Config.API_KEY)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body)
                }
            }
            Result.Error(IOException("Error fetching trending movie ${response.code()} ${response.message()}"))
        },
        errorMessage = "Error fetching trending movie page $page"
    )
}
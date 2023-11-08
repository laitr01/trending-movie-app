package com.trachlai.trendingmovieapp.data.source.remote

import com.trachlai.trendingmovieapp.common.safeApiCall
import java.io.IOException
import javax.inject.Inject
import com.trachlai.trendingmovieapp.common.Result as Result

class MovieRemoteDataSourceImpl @Inject constructor(
    private val movieService: MovieService,
    private val trendingService: TrendingMovieService
) : MovieRemoteDataSource {
    override suspend fun searchMovies(
        version: Int,
        query: String,
        page: Int
    ): Result<RemoteMovieResponse> = safeApiCall(
        call = {
            val response = movieService.searchMovies(version, query, page)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return@safeApiCall Result.Success(body)
                }
            }
            Result.Error(IOException("Error searching movie ${response.code()} ${response.message()}"))
        },
        errorMessage = "Error fetching search movie, keyword $query"
    )


    override suspend fun fetchTrendingMovies(
        version: Int,
        time: String,
        page: Int
    ): Result<RemoteMovieResponse> = safeApiCall(
        call = {
            val response = trendingService.fetchTrendingMovies(version, time, page)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return@safeApiCall Result.Success(body)
                }
            }
            return@safeApiCall Result.Error(IOException("Error fetching trending movie ${response.code()} ${response.message()}"))
        },
        errorMessage = "Error fetching trending movie page $page"
    )

    override suspend fun fetchTrendingMovieDetail(
        version: Int,
        movieId: Long
    ): Result<RemoteMovieDetail> =
        safeApiCall(
            call = {
                val response =
                    movieService.requestMovieDetail(version, movieId)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        return@safeApiCall Result.Success(body)
                    }
                }
                return@safeApiCall Result.Error(IOException("Error fetching movie detail ${response.code()} ${response.message()}"))
            },
            errorMessage = "Error fetching movie detail for movie id $movieId"
        )
}
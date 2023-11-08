package com.trachlai.trendingmovieapp.data

import android.util.Log
import com.trachlai.trendingmovieapp.common.Result
import com.trachlai.trendingmovieapp.data.source.MovieDetail
import com.trachlai.trendingmovieapp.data.source.remote.MovieRemoteDataSource
import com.trachlai.trendingmovieapp.data.source.room.MovieDao
import com.trachlai.trendingmovieapp.data.source.room.MovieDetailDao
import com.trachlai.trendingmovieapp.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val remoteMovieDataSource: MovieRemoteDataSource,
    private val movieDao: MovieDao,
    private val movieDetailDao: MovieDetailDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : MovieRepository {
    override suspend fun getTrendingMovies(
        version: Int, page: Int, windowTime: String, forceUpdate: Boolean
    ): Result<MovieModel> {
        val maxPage = movieDao.getMaxPage() ?: 0
        if (forceUpdate || page > maxPage) {
            //Log.e("DEBUG", "forceUpdate ---> $forceUpdate, page ---> $page, max page ---> $maxPage")
            val exception = refresh(version, page, windowTime)
            if (exception != null) {
                return Result.Error(exception)
            }
        }
        val resp = movieDao.getTrendingMoviesBy(page)
        return if (resp == null) {
            Result.Success(MovieModel(page, emptyList()))
        } else {
            Result.Success(MovieModel(page, resp.toMovieList()))
        }
    }

    override suspend fun requestSearchMovie(
        version: Int, page: Int, query: String
    ): Result<MovieModel> {
        Log.e("DEBUG", "requestSearchMovie ---> page ---> $page, query ---> $query")
        return when (val resp = remoteMovieDataSource.searchMovies(version, query, page)) {
            is Result.Error -> resp
            is Result.Success -> {
                Result.Success(MovieModel(page, resp.data.results.map { it.toMovie() }))
            }
        }
    }

    override suspend fun fetchTrendingMovieDetail(
        version: Int,
        movieId: Long
    ): Result<MovieDetail> {
        val resp = movieDetailDao.getMovieDetailBy(movieId)
        return if (resp == null) {
            when (val result =
                remoteMovieDataSource.fetchTrendingMovieDetail(version, movieId)) {
                is Result.Error -> result
                is Result.Success -> {
                    movieDetailDao.upsertMovieDetail(result.data.toLocalMovieDetail())
                    Result.Success(result.data.toMovieDetail())
                }
            }
        } else {
            Result.Success(resp.toMovieDetail())
        }
    }

    override suspend fun refresh(version: Int, page: Int, windowTime: String): Exception? {
        return withContext(dispatcher) {
            when (val resp = remoteMovieDataSource.fetchTrendingMovies(version, windowTime, page)) {
                is Result.Error -> {
                    return@withContext resp.exception
                }

                is Result.Success -> {
                    movieDao.deleteTrendingMovies()
                    movieDao.upsertTrendingMovie(resp.data.results.toLocalMovie(page))
                    return@withContext null
                }
            }
        }
    }
}
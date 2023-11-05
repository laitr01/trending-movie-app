package com.trachlai.trendingmovieapp.data

import android.util.Log
import com.trachlai.trendingmovieapp.data.source.local.MovieDao
import com.trachlai.trendingmovieapp.data.source.remote.MovieRemoteDataSource
import com.trachlai.trendingmovieapp.di.ApplicationScope
import com.trachlai.trendingmovieapp.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject
import com.trachlai.trendingmovieapp.utils.Result as Result

class MovieRepositoryImpl @Inject constructor(
    private val remoteMovieDataSource: MovieRemoteDataSource,
    private val movieDao: MovieDao,
    @DefaultDispatcher  private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : MovieRepository {
    override suspend fun getTrendingMovies(
        version: Int, page: Int, windowTime: String, forceUpdate: Boolean
    ): Result<List<Movie>> {
        if (forceUpdate) {
            val exception = refresh(version, page, windowTime)
            if (exception != null) {
                return Result.Error(exception)
            }
        }
        val resp = movieDao.getTrendingMoviesBy(page)
        return if (resp == null) {
            Result.Success(emptyList())
        } else {
            Result.Success(resp.toMovieList())
        }
    }

    override suspend fun requestSearchMovie(
        version: Int, page: Int, query: String
    ): Result<List<Movie>> {
        when (val resp = remoteMovieDataSource.searchMovies(page, query, page)) {
            is Result.Error -> resp
            is Result.Success -> {
                return Result.Success(resp.data.results.map { it.toMovie() })
            }
        }
        return Result.Error(Exception("Exception happened when request search movie."))
    }

    override suspend fun fetchTrendingMovieDetail(version: Int, movieId: Long): Result<Movie> {
        val resp = remoteMovieDataSource.fetchTrendingMovieDetail(version, movieId)
        when (resp) {
            is Result.Error -> resp
            is Result.Success -> {
                return Result.Success(resp.data.toMovie())
            }
        }
        return Result.Error(Exception("Exception happened when request movie detail id: $movieId."))
    }

    override suspend fun refresh(version: Int, page: Int, windowTime: String): Exception? {
        return withContext(dispatcher) {
            when (val resp = remoteMovieDataSource.fetchTrendingMovies(version, windowTime, page)) {
                is Result.Error -> {
                    resp.exception
                }

                is Result.Success -> {
                    movieDao.deleteTrendingMovies(page)
                    movieDao.upsertTrendingMovie(resp.data.results.toLocalMovie(page) )
                    null
                }
            }
        }
    }
}
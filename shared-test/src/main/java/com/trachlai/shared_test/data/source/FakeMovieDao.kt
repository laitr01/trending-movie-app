package com.trachlai.shared_test.data.source

import com.trachlai.trendingmovieapp.data.source.room.LocalMovie
import com.trachlai.trendingmovieapp.data.source.room.MovieDao

class FakeMovieDao(initMovies: List<LocalMovie> = emptyList()) : MovieDao {
    private val _movies = mutableMapOf<Int, LocalMovie?>()

    init {
        for (movie in initMovies) {
            _movies[movie.page] = movie
        }
    }

    override suspend fun getTrendingMoviesBy(page: Int): LocalMovie? {
        return _movies[page]
    }

    override suspend fun upsertTrendingMovie(movie: LocalMovie) {
        _movies[movie.page] = movie
    }

    override suspend fun deleteTrendingMovies() {
        _movies.clear()
    }

    override suspend fun getMaxPage(): Int {
        return _movies.keys.max().or(0)
    }

}
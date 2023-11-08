package com.trachlai.shared_test.data.source

import com.trachlai.trendingmovieapp.data.source.room.LocalMovie
import com.trachlai.trendingmovieapp.data.source.room.MovieDao

class FakeMovieDao(initMovies: List<LocalMovie>? = emptyList()): MovieDao {
    private var _movies : MutableMap<Int, LocalMovie>? =null

    var movies: List<LocalMovie>?
        get() = _movies?.values?.toList()
        set(newTasks) {
            _movies = newTasks?.associateBy { it.page }?.toMutableMap()
        }

    init {
        movies = initMovies
    }
    override suspend fun getTrendingMoviesBy(page: Int): LocalMovie? {
        return _movies?.get(page)
    }

    override suspend fun upsertTrendingMovie(movie: LocalMovie) {
        _movies?.put(movie.page,movie)
    }

    override suspend fun deleteTrendingMovies() {
        TODO("Not yet implemented")
    }

    override suspend fun getMaxPage(): Int? {
        TODO("Not yet implemented")
    }

}
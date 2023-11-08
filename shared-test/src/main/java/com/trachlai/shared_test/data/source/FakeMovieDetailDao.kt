package com.trachlai.shared_test.data.source

import com.trachlai.trendingmovieapp.data.source.room.LocalMovie
import com.trachlai.trendingmovieapp.data.source.room.LocalMovieDetail
import com.trachlai.trendingmovieapp.data.source.room.MovieDao
import com.trachlai.trendingmovieapp.data.source.room.MovieDetailDao

class FakeMovieDetailDao(initMovies: List<LocalMovie> = emptyList()) : MovieDetailDao {
    override suspend fun getMovieDetailBy(id: Long): LocalMovieDetail? {
        TODO("Not yet implemented")
    }

    override suspend fun upsertMovieDetail(movie: LocalMovieDetail) {
        TODO("Not yet implemented")
    }

}
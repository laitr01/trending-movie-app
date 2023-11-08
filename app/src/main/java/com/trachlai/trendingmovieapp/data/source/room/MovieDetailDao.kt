package com.trachlai.trendingmovieapp.data.source.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MovieDetailDao {
    @Query("select * from movie_detail where id = :id")
    suspend fun getMovieDetailBy(id: Long): LocalMovieDetail?

    @Upsert
    suspend fun upsertMovieDetail(movie: LocalMovieDetail)
}
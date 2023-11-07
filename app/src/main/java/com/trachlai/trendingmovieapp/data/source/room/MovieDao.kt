package com.trachlai.trendingmovieapp.data.source.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MovieDao {
    @Query("select * from trending_movie where page = :page")
    suspend fun getTrendingMoviesBy(page: Int): LocalMovie?
    @Upsert
    suspend fun upsertTrendingMovie(movie: LocalMovie)
    @Query("delete from trending_movie")
    suspend fun deleteTrendingMovies()

    @Query("select max(page) from trending_movie")
    suspend fun getMaxPage(): Int
}
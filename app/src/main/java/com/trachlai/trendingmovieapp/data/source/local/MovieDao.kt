package com.trachlai.trendingmovieapp.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MovieDao {
    @Query("select * from trending_movie where page = :page")
    suspend fun getTrendingMoviesBy(page: Int): LocalMovie?
    @Upsert
    suspend fun upsertTrendingMovie(movie: LocalMovie)
    @Query("delete from trending_movie where page = :page")
    suspend fun deleteTrendingMovies(page :Int)
}
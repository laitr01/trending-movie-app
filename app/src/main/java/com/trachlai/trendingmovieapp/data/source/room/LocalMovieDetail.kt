package com.trachlai.trendingmovieapp.data.source.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movie_detail",
    indices = [Index(value = ["id"])]
)
data class LocalMovieDetail (
    @PrimaryKey
    val id : Long,
    val data: String,
)
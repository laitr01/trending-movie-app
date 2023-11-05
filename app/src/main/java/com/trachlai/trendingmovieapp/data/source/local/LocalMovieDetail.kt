package com.trachlai.trendingmovieapp.data.source.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movie_detail",
    indices = [Index(value = ["page"])]
)
data class LocalMovieDetail (
    @PrimaryKey
    val id : Long,
    val data: String,
)
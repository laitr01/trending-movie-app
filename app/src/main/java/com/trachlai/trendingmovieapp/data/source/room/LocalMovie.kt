package com.trachlai.trendingmovieapp.data.source.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "trending_movie",
    indices = [Index(value = ["page"])]
)
data class LocalMovie (
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val page: Int,
    val data: String,
)
package com.trachlai.trendingmovieapp.data

data class Movie(
    val id: Long,
    val title: String,
    val posterUrl: String,
    val releaseDate: String,
    val voteAverage: Float
)
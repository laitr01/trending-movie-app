package com.trachlai.trendingmovieapp.data.source


data class MovieDetail(
    val genres: String,
    val homepage: String,
    val id: Long,
    val language: String,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val releaseDate: String,
    val runtime: String,
    val voteAverage: Float,
    val voteCount: Int
)
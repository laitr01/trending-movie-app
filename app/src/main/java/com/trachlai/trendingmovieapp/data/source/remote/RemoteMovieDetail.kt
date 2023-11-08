package com.trachlai.trendingmovieapp.data.source.remote

data class RemoteMovieDetail(
    val genres: List<RemoteGenre>?,
    val homepage: String?,
    val id: Long?,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val runtime: Int?,
    val vote_average: Float?,
    val vote_count: Int?
)

data class RemoteGenre(
    val id: Int?,
    val name: String?
)

package com.trachlai.trendingmovieapp.data.source.remote

data class RemoteMovie (
    val id: Long,
    val poster_path: String?,
    val vote_average: Float?,
    val release_date: String?,
    val original_title: String?
)
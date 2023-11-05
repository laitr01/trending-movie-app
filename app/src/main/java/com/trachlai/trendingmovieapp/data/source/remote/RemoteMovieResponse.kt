package com.trachlai.trendingmovieapp.data.source.remote

data class RemoteMovieResponse (
    val page: Int,
    val results: List<RemoteMovie>,
    val total_pages: Int,
    val total_results: Int
)
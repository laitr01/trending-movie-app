package com.trachlai.trendingmovieapp

object Config {
    val BASE_URL = "https://api.themoviedb.org"
    val API_KEY = "47aa75b56464da7a186b813a50035cd4"
    val API_VERSION = 3
    private const val BASE_POSTER_PATH = "https://image.tmdb.org/t/p/w342"

    @JvmStatic
    fun getPosterPath(posterPath: String?): String {
        return BASE_POSTER_PATH + posterPath
    }

    fun getCachedTrendingVideoDuration() : Long = 1 * 60 * 1000
}
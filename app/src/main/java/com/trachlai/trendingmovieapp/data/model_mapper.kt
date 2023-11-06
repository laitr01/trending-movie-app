package com.trachlai.trendingmovieapp.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trachlai.trendingmovieapp.Config
import com.trachlai.trendingmovieapp.data.source.room.LocalMovie
import com.trachlai.trendingmovieapp.data.source.remote.RemoteMovie
import com.trachlai.trendingmovieapp.utils.orValue

fun RemoteMovie.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.original_title.orValue("<Missing title>"),
        posterUrl = Config.getPosterPath(this.poster_path),
        releaseDate = this.release_date.orValue("Missing release date>"),
        voteAverage = this.vote_average.orValue(0.0f) / 2
    )
}

fun List<RemoteMovie>.toLocalMovie(page: Int): LocalMovie {
    val type = object : TypeToken<List<RemoteMovie>>() {}.type
    val data = Gson().toJson(this, type)
    return LocalMovie(
        page = page,
        data = data
    )
}


fun LocalMovie.toMovieList(): List<Movie> {
    if (this.data.isEmpty()) {
        return emptyList()
    }
    val typeOf = object : TypeToken<Collection<Movie>>() {}.type
    return Gson().fromJson(this.data, typeOf)
}

package com.trachlai.trendingmovieapp.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trachlai.trendingmovieapp.Config
import com.trachlai.trendingmovieapp.common.orValue
import com.trachlai.trendingmovieapp.data.source.MovieDetail
import com.trachlai.trendingmovieapp.data.source.remote.RemoteMovie
import com.trachlai.trendingmovieapp.data.source.remote.RemoteMovieDetail
import com.trachlai.trendingmovieapp.data.source.room.LocalMovie
import com.trachlai.trendingmovieapp.data.source.room.LocalMovieDetail

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

fun RemoteMovieDetail.toLocalMovieDetail(): LocalMovieDetail {
    val type = object : TypeToken<RemoteMovieDetail>() {}.type
    val data = Gson().toJson(this, type)
    return LocalMovieDetail(
        id = id.orValue(-1),
        data = data
    )
}


fun LocalMovie.toMovieList(): List<Movie> {
    if (this.data.isEmpty()) {
        return emptyList()
    }
    //Log.e("DEBUG", this.data)
    val typeOf = object : TypeToken<Collection<RemoteMovie>>() {}.type
    val list = Gson().fromJson<List<RemoteMovie>>(this.data, typeOf)
    return list.map { it.toMovie() }
}

fun RemoteMovieDetail.toMovieDetail(): MovieDetail {
    return MovieDetail(
        genres = genres.orEmpty().map { it.name }.joinToString(" | "),
        homepage = homepage.orEmpty(),
        id = id.orValue(-1),
        language = original_language.orValue("Missing original_language"),
        title = title.orValue("missing title"),
        overview = overview.orValue("missing overview"),
        posterUrl = Config.getPosterPath(poster_path),
        releaseDate = release_date.orValue("missing release_date"),
        runtime = "${runtime.orValue(0)} minutes",
        voteAverage = vote_average.orValue(0.0f) / 2,
        voteCount = vote_count.orValue(0)
    )
}

fun LocalMovieDetail.toMovieDetail(): MovieDetail {
    val typeOf = object : TypeToken<RemoteMovieDetail>() {}.type
    val detail = Gson().fromJson<RemoteMovieDetail>(this.data, typeOf)
    return detail.toMovieDetail()
}
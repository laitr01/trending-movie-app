package com.trachlai.trendingmovieapp.movie_listing

import com.trachlai.trendingmovieapp.data.Movie
import com.trachlai.trendingmovieapp.data.MovieModel
import com.trachlai.trendingmovieapp.utils.UIState

enum class ViewType(val id: Int) {
    Search(1),
    Trending(2);

    companion object {

        private val values = values()
        fun of(id: Int): ViewType? {
            return values.firstOrNull { it.id == id }
        }
    }
}

enum class UiState {
    Loading,
    Error,
    Success,
    Empty
}

enum class Action {
    Reload,
    FirstLoad,
    LoadMore,
    Retry
}

data class MovieListingUiModel(
    private var viewType: ViewType = ViewType.Trending,
    private var uiState: UiState = UiState.Loading,
    private var action: Action = Action.FirstLoad,
    private var movies: MutableList<Movie> = mutableListOf(),
    private var hasNext: Boolean = false,
    private var page: Int = 1
) {

    fun action() = action
    fun uiState() = uiState
    fun movieList() = movies

    fun hasNext() = hasNext

    fun page() = page

    fun viewType() = viewType
    fun doUpdate(
        viewType: ViewType? = null,
        uiState: UiState? = null,
        action: Action? = null,
        movies: MutableList<Movie>? = null,
        hasNext: Boolean? = null,
        page: Int? = null
    ): MovieListingUiModel {
        viewType?.let { this.viewType = viewType }
        uiState?.let { this.uiState = uiState }
        action?.let { this.action = action }
        movies?.let { this.movies = movies }
        hasNext?.let { this.hasNext = hasNext }
        page?.let { this.page = page }
        return this
    }
}
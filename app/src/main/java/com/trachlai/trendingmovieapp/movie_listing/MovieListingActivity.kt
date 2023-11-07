package com.trachlai.trendingmovieapp.movie_listing

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.trachlai.trendingmovieapp.data.Movie
import com.trachlai.trendingmovieapp.databinding.ActivityMovieListingBinding
import com.trachlai.trendingmovieapp.movie_listing.search.RecentSearchAdapter
import com.trachlai.trendingmovieapp.utils.OnScrollListener
import com.trachlai.trendingmovieapp.utils.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieListingBinding
    private val viewModel: MovieListingViewModel by viewModels()
    private lateinit var movieListingAdapter: MovieListingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMovieListingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        movieListingAdapter = MovieListingAdapter()
        binding.movieListingRecyclerView.apply {
            layoutManager = GridLayoutManager(this@MovieListingActivity, 2)
            adapter = movieListingAdapter
            addOnScrollListener(object : OnScrollListener() {
                override fun onScrollToBottom() {
                    viewModel.loadNext(binding.searchView?.text.toString())
                }
            })
        }

        with(binding) {
            searchView.editText.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchBar.text = searchView.text
                    searchFor(searchView.text.toString())
                    searchView.hide()
                    return@setOnEditorActionListener true
                }
                false
            }
            searchView.setupWithSearchBar(searchBar)
        }

        viewModel.recentQueries.observe(this) {
            binding.queryHistoryRecylerView?.apply {
                layoutManager = LinearLayoutManager(this@MovieListingActivity)
                adapter = RecentSearchAdapter(this@MovieListingActivity, it)
            }
        }

        viewModel.uiMovieListingLiveData.observe(this) {
            handleUiRendering(it)
        }
    }

    private fun handleUiRendering(model: MovieListingUiModel?) {
        model?.uiState()?.let {
            Log.e("DEBUG", model.toString())
            when (it) {
                UiState.Error -> {
                    renderErrorState()
                }

                UiState.Empty -> {
                    renderEmptyState()
                }

                UiState.Loading -> {
                    renderLoadingState()
                }

                UiState.Success -> {
                    renderData(model.movieList(), model.action())
                }
            }
        } ?: renderEmptyState()
    }

    private fun renderLoadingState() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            emptyScreen.root.visibility = View.GONE
            errorScreen.root.visibility = View.GONE
            movieListingRecyclerView.visibility = View.GONE
        }
    }

    private fun renderData(movies: List<Movie>, action: Action) {
        with(binding) {
            progressBar.visibility = View.GONE
            emptyScreen.root.visibility = View.GONE
            errorScreen.root.visibility = View.GONE
            movieListingRecyclerView.visibility = View.VISIBLE
            movieListingAdapter.addMovieList(movies)
        }
    }

    private fun renderEmptyState() {
        with(binding) {
            progressBar.visibility = View.GONE
            emptyScreen.root.visibility = View.VISIBLE
            errorScreen.root.visibility = View.GONE
            movieListingRecyclerView.visibility = View.GONE
        }
    }

    private fun renderErrorState() {
        with(binding) {
            progressBar.visibility = View.GONE
            emptyScreen.root.visibility = View.GONE
            errorScreen.root.visibility = View.VISIBLE
            movieListingRecyclerView.visibility = View.GONE
        }
    }

    private fun searchFor(query: String) {
        viewModel.reload(query)
    }

}
package com.trachlai.trendingmovieapp.movie_listing

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.trachlai.trendingmovieapp.databinding.ActivityMovieListingBinding
import com.trachlai.trendingmovieapp.movie_listing.search.RecentSearchAdapter
import com.trachlai.trendingmovieapp.utils.UIState
import dagger.hilt.android.AndroidEntryPoint
import com.trachlai.trendingmovieapp.utils.Result as Result

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

        binding.movieListingRecyclerView?.apply {
            layoutManager = GridLayoutManager(this@MovieListingActivity, 2)
            adapter = movieListingAdapter
        }

        with(binding) {
            searchView?.editText?.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchBar?.text = searchView.text
                    searchFor(searchView.text.toString())
                    searchView.hide()
                    return@setOnEditorActionListener true
                }
                false
            }
            searchView?.setupWithSearchBar(searchBar)
        }

        viewModel.recentQueries.observe(this) {
            binding.queryHistoryRecylerView?.apply {
                layoutManager = LinearLayoutManager(this@MovieListingActivity)
                adapter = RecentSearchAdapter(this@MovieListingActivity, it)
            }
        }

        viewModel.moviesListing.observe(this) {
            when (it) {
                is UIState.Loading -> {
                    binding.progressBar?.visibility = View.VISIBLE
                }

                is UIState.Success -> {
                    binding.progressBar?.visibility = View.GONE
                    movieListingAdapter.clear()
                    movieListingAdapter.addMovieList(it.data.movies)
                }

                is UIState.Failure -> {
                    binding.progressBar?.visibility = View.GONE
                }

                else -> {
                    binding.progressBar?.visibility = View.GONE
                }
            }
        }
    }

    private fun searchFor(query: String) {
        viewModel.searchFor(query)
    }

}
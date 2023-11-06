package com.trachlai.trendingmovieapp.trending_movie_listing

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.trachlai.trendingmovieapp.databinding.ActivityMainBinding
import com.trachlai.trendingmovieapp.trending_movie_listing.search.RecentSearchAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrendingMovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: TrendingMovieListingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            searchView?.editText?.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchBar?.text = searchView.text
                    searchView.hide()
                    return@setOnEditorActionListener true
                }
                false
            }
            searchView?.setupWithSearchBar(searchBar)
        }

        viewModel.recentQueries.observe(this) {
            binding.queryHistoryRecylerView?.apply {
                layoutManager = LinearLayoutManager(this@TrendingMovieActivity)
                adapter = RecentSearchAdapter(this@TrendingMovieActivity, it)
            }
        }
    }



}
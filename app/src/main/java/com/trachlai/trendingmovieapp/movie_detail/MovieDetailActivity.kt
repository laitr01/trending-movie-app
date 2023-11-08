package com.trachlai.trendingmovieapp.movie_detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.trachlai.trendingmovieapp.common.UIState
import com.trachlai.trendingmovieapp.common.openUrl
import com.trachlai.trendingmovieapp.data.source.MovieDetail
import com.trachlai.trendingmovieapp.databinding.ActivityMovieDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailBinding
    private val viewModel: MovieDetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.movieDetailLiveData.observe(this) {
            handleUiRendering(it)
        }

        binding.errorScreen.errorScreenButton.setOnClickListener {
            viewModel.fetchMovieDetail(intent)
        }

        binding.homepage.setOnClickListener { openUrl(binding.homepage.text.toString()) }

        viewModel.fetchMovieDetail(intent)
    }

    private fun handleUiRendering(model: UIState<MovieDetail>) {
        Log.e("DEBUG", model.toString())
        when (model) {
            is UIState.Loading -> {
                renderLoadingState()
            }

            is UIState.Failure -> {
                renderErrorState()
            }

            is UIState.Success -> {
                renderData(model.data)
            }

            else -> {}
        }
    }

    private fun renderLoadingState() {
        with(binding) {
            errorScreen.root.visibility = View.GONE
            movieImageProgressBar.visibility = View.VISIBLE
            moviesLayout.visibility = View.GONE
            movieImage.visibility = View.VISIBLE
        }
    }

    private fun renderData(movie: MovieDetail) {
        with(binding) {
            errorScreen.root.visibility = View.GONE
            movieImageProgressBar.visibility = View.GONE
            moviesLayout.visibility = View.VISIBLE
            movieImage.visibility = View.VISIBLE

            titleText.text = movie.title
            Glide.with(movieImage).load(movie.posterUrl).into(movieImage)
            ratingBar.rating = movie.voteAverage
            releaseDateText.text = movie.releaseDate
            votes.text = "%.1f (%d votes)".format(movie.voteAverage, movie.voteCount)
            runtimeText.text = movie.runtime
            languageText.text = movie.language
            genresText.text = movie.genres
            overviewText.text = movie.overview
            homepage.visibility = if (movie.homepage.isEmpty()) View.GONE else View.VISIBLE
            homepage.text = movie.homepage
        }
    }

    private fun renderErrorState() {
        with(binding) {
            errorScreen.root.visibility = View.VISIBLE
            moviesLayout.visibility = View.GONE
            movieImageProgressBar.visibility = View.GONE
            movieImage.visibility = View.GONE
        }
    }

    companion object {
        fun start(context: Context, movieId: Long) {
            context.startActivity(Intent(context, MovieDetailActivity::class.java).apply {
                putExtra(MovieDetailViewModel.MOVIE_ID, movieId)
            })
        }
    }
}
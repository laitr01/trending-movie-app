package com.trachlai.trendingmovieapp.movie_listing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trachlai.trendingmovieapp.data.Movie
import com.trachlai.trendingmovieapp.databinding.MovieListingItemBinding

class MovieListingAdapter : RecyclerView.Adapter<MovieListingAdapter.MovieListingViewHolder>() {

    private val movies: MutableList<Movie> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListingViewHolder {
        val binding = MovieListingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieListingViewHolder(binding).apply {
            binding.root.setOnClickListener {
                val movie = adapterPosition.takeIf { it != RecyclerView.NO_POSITION }
                    ?: return@setOnClickListener
                //MovieDetailActivity.startActivityModel(it.context, items[movie])
            }
        }
    }

    override fun onBindViewHolder(holder: MovieListingViewHolder, position: Int) {
        with(holder.binding) {
            val movie = movies[position]
            title.text = movie.title
            Glide.with(poster)
                .load(movie.posterUrl)
                .into(poster)
            ratingBar.rating = movie.voteAverage
            releaseDate.text = movie.releaseDate
        }
    }

    fun addMovieList(movies: List<Movie>) {
        val previousItemSize = this.movies.size
        this.movies.addAll(movies)
        notifyItemRangeInserted(previousItemSize, movies.size)
    }

    fun clear() {
        movies.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = movies.size

    class MovieListingViewHolder(val binding: MovieListingItemBinding) : RecyclerView.ViewHolder(binding.root)
}
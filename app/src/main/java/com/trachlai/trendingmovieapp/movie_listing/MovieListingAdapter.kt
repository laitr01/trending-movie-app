package com.trachlai.trendingmovieapp.movie_listing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.trachlai.trendingmovieapp.R
import com.trachlai.trendingmovieapp.data.Movie
import com.trachlai.trendingmovieapp.databinding.HeaderLayoutBinding
import com.trachlai.trendingmovieapp.databinding.MovieListingItemBinding


class MovieListingAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val movies: MutableList<Movie> = arrayListOf()
    private var viewType: ViewType = ViewType.Trending
    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1
    private val VIEW_TYPE_FOOTER = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            HeaderLayoutViewHolder(
                HeaderLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            MovieListingViewHolder(
                MovieListingItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            ).apply {
                binding.root.setOnClickListener {
                    val movie = adapterPosition.takeIf { it != RecyclerView.NO_POSITION }
                        ?: return@setOnClickListener
                    //MovieDetailActivity.startActivityModel(it.context, items[movie])
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is MovieListingViewHolder -> {
                with(holder.binding) {
                    val movie = movies[position - 1]
                    title.text = movie.title
                    Glide.with(poster)
                        .load(movie.posterUrl)
                        .into(poster)
                    ratingBar.rating = movie.voteAverage
                    releaseDate.text = movie.releaseDate
                    averageVote.text = "%.1f".format(movie.voteAverage)
                }
            }

            is HeaderLayoutViewHolder -> {
                with(holder.binding) {
                    headerText.text =
                        root.context.getText(if (viewType == ViewType.Trending) R.string.trending_header_text else R.string.search_header_text)
                }
            }
        }
    }

    fun addMovieList(movies: List<Movie>) {
        val diffCallback = MovieDiffCallback(this.movies, movies)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.movies.clear()
        this.movies.addAll(movies)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateViewType(type: ViewType) {
        viewType = type
        notifyItemChanged(0)
    }

    fun clear() {
        movies.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = movies.size + 1

    class MovieListingViewHolder(val binding: MovieListingItemBinding) :
        ViewHolder(binding.root)

    class HeaderLayoutViewHolder(val binding: HeaderLayoutBinding) : ViewHolder(binding.root)

}
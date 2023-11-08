package com.trachlai.trendingmovieapp.movie_listing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.trachlai.trendingmovieapp.R
import com.trachlai.trendingmovieapp.common.DiffCallback
import com.trachlai.trendingmovieapp.data.Movie
import com.trachlai.trendingmovieapp.databinding.HeaderLayoutBinding
import com.trachlai.trendingmovieapp.databinding.MovieListingItemBinding
import com.trachlai.trendingmovieapp.databinding.ProgressBarLayoutBinding


class MovieListingAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var movies = mutableListOf<Movie>()
    private var viewType: ViewType = ViewType.Trending
    private var hasNext = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                HeaderLayoutViewHolder(
                    HeaderLayoutBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }

            VIEW_TYPE_ITEM -> {
                MovieListingViewHolder(
                    MovieListingItemBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                ).apply {
                    binding.root.setOnClickListener {
                        val movie = adapterPosition.takeIf { it != RecyclerView.NO_POSITION }
                            ?: return@setOnClickListener
                        //MovieDetailActivity.startActivityModel(it.context, items[movie])
                    }
                }
            }

            else -> {
                ProgressLayoutViewHolder(
                    ProgressBarLayoutBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_HEADER
        } else if (position < this.movies.size) {
            VIEW_TYPE_ITEM
        } else {
            VIEW_TYPE_FOOTER
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is MovieListingViewHolder -> {
                with(holder.binding) {
                    val movie = movies[position]
                    title.text = movie.title
                    Glide.with(poster).load(movie.posterUrl).into(poster)
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

            is ProgressLayoutViewHolder -> {
                with(holder.binding) {
                    root.visibility = if (hasNext) View.VISIBLE else View.GONE
                }
            }
        }
    }

    fun addMovieList(movies: MutableList<Movie>, newHasNext: Boolean, action: Action) {
        if (action != Action.LoadMore) {
            movies.add(0, Movie(-1, "", "", "", 0.0f))
        }
        if (hasNext) {
            hasNext = false
            notifyItemRemoved(movies.size)
        }

        val result = DiffUtil.calculateDiff(DiffCallback(callback, this.movies, movies))
        this.movies = movies.toMutableList()
        result.dispatchUpdatesTo(this)

        hasNext = newHasNext
        if (hasNext) {
            notifyItemInserted(this.movies.size)
        }
    }

    fun updateViewType(type: ViewType) {
        viewType = type
        notifyItemChanged(0)
    }

    override fun getItemCount(): Int = movies.size + if (hasNext) 1 else 0

    class MovieListingViewHolder(val binding: MovieListingItemBinding) : ViewHolder(binding.root)

    class HeaderLayoutViewHolder(val binding: HeaderLayoutBinding) : ViewHolder(binding.root)
    class ProgressLayoutViewHolder(val binding: ProgressBarLayoutBinding) : ViewHolder(binding.root)

    companion object {

        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_ITEM = 1
        const val VIEW_TYPE_FOOTER = 2

        private val callback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(
                oldItem: Movie, newItem: Movie
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Movie, newItem: Movie
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
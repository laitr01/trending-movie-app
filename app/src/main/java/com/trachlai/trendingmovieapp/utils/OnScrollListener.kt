package com.trachlai.trendingmovieapp.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class OnScrollListener(private val itemCountPerFetch: Int = -1) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
            ?: throw IllegalStateException("layoutManager must extends LinearLayoutManager")

        val visibleItemCount = layoutManager.childCount
        val pastVisibleItemCount = layoutManager.findFirstVisibleItemPosition()
        val totalItemCount = layoutManager.itemCount

        if (visibleItemCount + pastVisibleItemCount >= totalItemCount &&
            (itemCountPerFetch < 0 || totalItemCount >= itemCountPerFetch)) {
            onScrollToBottom()
        }
    }

    abstract fun onScrollToBottom()
}
package com.trachlai.trendingmovieapp.common

import androidx.recyclerview.widget.DiffUtil

class DiffCallback<T : Any>(
    private val callback: DiffUtil.ItemCallback<T>,
    private val oldList: List<T>,
    private val newList: List<T>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return callback.areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return callback.areContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }
}
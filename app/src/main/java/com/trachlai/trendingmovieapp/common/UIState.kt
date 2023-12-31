package com.trachlai.trendingmovieapp.common

sealed class UIState<out R> {
    object Loading : UIState<Nothing>()
    data class Success<T>(val data: T) : UIState<T>()
    data class Failure(val exception: Exception) : UIState<Nothing>()
}
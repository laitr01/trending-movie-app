package com.trachlai.trendingmovieapp.common

fun String?.orValue(default: String) : String {
    if (this == null) {
        return default
    }
    return this
}

fun Float?.orValue(default: Float) : Float {
    if (this == null) {
        return default
    }
    return this
}
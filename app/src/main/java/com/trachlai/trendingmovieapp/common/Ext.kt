package com.trachlai.trendingmovieapp.common

import android.app.Activity
import android.content.Intent
import android.net.Uri

fun String?.orValue(default: String): String {
    if (this == null) {
        return default
    }
    return this
}

fun Float?.orValue(default: Float): Float {
    if (this == null) {
        return default
    }
    return this
}

fun Int?.orValue(default: Int): Int {
    if (this == null) {
        return default
    }
    return this
}

fun Long?.orValue(default: Long): Long {
    if (this == null) {
        return default
    }
    return this
}

fun Activity.openUrl(uri: String) {
    startActivity( Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
}

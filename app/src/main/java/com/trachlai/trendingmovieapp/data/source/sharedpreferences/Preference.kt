package com.trachlai.trendingmovieapp.data.source.sharedpreferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationPreference @Inject constructor(@ApplicationContext context: Context) {
    private val SHARED_PREF = "SHARED_PREF"
    private val prefs = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
    private val TRENDING_VIDEOS_CACHED_TIME_KEY = "TRENDING_VIDEOS_CACHED_TIME_KEY"
    private val RECENT_QUERIES = "RECENT_QUERIES"
    fun getCachedTime(): Long {
        return prefs.getLong(TRENDING_VIDEOS_CACHED_TIME_KEY, 0)
    }
    fun storeCachedTime(time: Long) {
        prefs.edit().putLong(TRENDING_VIDEOS_CACHED_TIME_KEY, time).apply()
    }
    fun getRecentQueries(): Set<String> {
        return prefs.getStringSet(RECENT_QUERIES, emptySet()).orEmpty()
    }

    fun storeRecentQueries(queries: Set<String>) {
        prefs.edit().putStringSet(RECENT_QUERIES, queries).apply()
    }
}
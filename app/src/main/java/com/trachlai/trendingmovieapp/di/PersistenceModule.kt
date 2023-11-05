package com.trachlai.trendingmovieapp.di

import android.content.Context
import androidx.room.Room
import com.trachlai.trendingmovieapp.data.source.local.MovieDao
import com.trachlai.trendingmovieapp.data.source.local.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {
    @Provides
    @Singleton
    fun provideRoomDataBase(@ApplicationContext context: Context): MovieDatabase {
        return Room
            .databaseBuilder(context, MovieDatabase::class.java, "movies.db")
            .allowMainThreadQueries()
            .build()
    }
    @Provides
    @Singleton
    fun provideMovieDao(database: MovieDatabase): MovieDao {
        return database.movieDao()
    }
}
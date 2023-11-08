package com.trachlai.trendingmovieapp.di

import com.trachlai.trendingmovieapp.data.MovieRepository
import com.trachlai.trendingmovieapp.data.MovieRepositoryImpl
import com.trachlai.trendingmovieapp.data.source.room.MovieDao
import com.trachlai.trendingmovieapp.data.source.remote.MovieRemoteDataSource
import com.trachlai.trendingmovieapp.data.source.room.MovieDetailDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideMovieRepository(
        movieRemoteDataSource: MovieRemoteDataSource,
        movieDao: MovieDao,
        movieDetailDao: MovieDetailDao,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): MovieRepository {
        return MovieRepositoryImpl(
            movieRemoteDataSource,
            movieDao,
            movieDetailDao,
            dispatcher,
        )
    }
}


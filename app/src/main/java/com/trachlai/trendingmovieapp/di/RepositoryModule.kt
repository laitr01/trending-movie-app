package com.trachlai.trendingmovieapp.di

import com.trachlai.trendingmovieapp.data.MovieRepository
import com.trachlai.trendingmovieapp.data.MovieRepositoryImpl
import com.trachlai.trendingmovieapp.data.source.local.MovieDao
import com.trachlai.trendingmovieapp.data.source.remote.MovieRemoteDataSource
import com.trachlai.trendingmovieapp.data.source.remote.MovieRemoteDataSourceImpl
import com.trachlai.trendingmovieapp.data.source.remote.MovieService
import com.trachlai.trendingmovieapp.data.source.remote.TrendingMovieService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideMovieRepository(
        movieRemoteDataSource: MovieRemoteDataSource,
        movieDao: MovieDao,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
    ) : MovieRepository {
        return MovieRepositoryImpl(movieRemoteDataSource, movieDao, dispatcher, scope)
    }
}


package com.trachlai.trendingmovieapp.di

import com.trachlai.trendingmovieapp.Config
import com.trachlai.trendingmovieapp.RequestInterceptor
import com.trachlai.trendingmovieapp.data.source.remote.MovieRemoteDataSource
import com.trachlai.trendingmovieapp.data.source.remote.MovieRemoteDataSourceImpl
import com.trachlai.trendingmovieapp.data.source.remote.MovieService
import com.trachlai.trendingmovieapp.data.source.remote.TrendingMovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(RequestInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Config.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideTrendingMovieService(retrofit: Retrofit) : TrendingMovieService {
        return retrofit.create(TrendingMovieService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieService(retrofit: Retrofit) : MovieService {
        return retrofit.create(MovieService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieRemoteDataSource(movieService: MovieService, trendingMovieService: TrendingMovieService) : MovieRemoteDataSource {
        return MovieRemoteDataSourceImpl(movieService, trendingMovieService)
    }
}
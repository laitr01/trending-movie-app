package com.trachlai.shared_test.di

import android.content.Context
import androidx.room.Room
import com.trachlai.trendingmovieapp.data.source.room.MovieDatabase
import com.trachlai.trendingmovieapp.di.PersistenceModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [PersistenceModule::class]
)
object DatabaseTestModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): MovieDatabase {
        return Room
            .inMemoryDatabaseBuilder(context.applicationContext, MovieDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
}
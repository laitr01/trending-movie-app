package com.trachlai.shared_test.di

import com.trachlai.shared_test.data.FakeTaskRepository
import com.trachlai.trendingmovieapp.data.MovieRepository
import com.trachlai.trendingmovieapp.di.RepositoryModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
object RepositoryTestModule {
    @Singleton
    @Provides
    fun provideTasksRepository(): MovieRepository {
        return FakeTaskRepository()
    }
}
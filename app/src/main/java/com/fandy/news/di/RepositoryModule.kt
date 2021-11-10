package com.fandy.news.di

import com.fandy.news.api.NewsService
import com.fandy.news.db.NewsDatabase
import com.fandy.news.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNewssterRepository(
        service: NewsService,
        database: NewsDatabase
    ): NewsRepository {
        return NewsRepository(service, database)
    }
}
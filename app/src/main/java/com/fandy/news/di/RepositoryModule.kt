package com.fandy.news.di

import com.fandy.news.api.LoginService
import com.fandy.news.api.NewsService
import com.fandy.news.db.NewsDatabase
import com.fandy.news.repository.LoginRepository
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
    fun provideNewsRepository(
        service: NewsService,
        database: NewsDatabase
    ): NewsRepository {
        return NewsRepository(service, database)
    }

    @Provides
    @Singleton
    fun provideOneRepository(service: LoginService): LoginRepository {
        return LoginRepository(service)
    }

}
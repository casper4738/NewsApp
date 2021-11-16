package com.fandy.news.di

import android.app.Application
import androidx.room.Room
import com.fandy.news.db.ArticleDao
import com.fandy.news.db.NewsDatabase
import com.fandy.news.db.RemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): NewsDatabase {
        return Room
            .databaseBuilder(app, NewsDatabase::class.java, "newsapp_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideArticleDao(newsDatabase: NewsDatabase): ArticleDao{
        return newsDatabase.articleDao()
    }

    @Provides
    fun provideRemoteKeysDao(newsDatabase: NewsDatabase): RemoteKeyDao{
        return newsDatabase.remoteKeyDao()
    }
}
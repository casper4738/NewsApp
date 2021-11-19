package com.fandy.news.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fandy.news.model.ArticleHome
import com.fandy.news.model.ArticleSearch
import com.fandy.news.model.ArticleTopHeadlines
import com.fandy.news.model.SourceConverter

@Database(
    entities = [
        ArticleHome::class,
        ArticleTopHeadlines::class,
        ArticleSearch::class,
        RemoteKey::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(value = [SourceConverter::class])
abstract class NewsDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}
package com.fandy.news.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.fandy.news.api.NewsService
import com.fandy.news.db.NewsDatabase
import com.fandy.news.model.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository implementation that uses a database backed
 * [androidx.paging.PagingSource] and [androidx.paging.RemoteMediator]
 * to load pages from network when there are no more items cached
 * in the database to load.
 */

@Singleton
class NewsRepository @Inject constructor(
    private val service: NewsService,
    private val database: NewsDatabase
) {

    @ExperimentalPagingApi
    fun fetchTopArticles(language: String, category: String): Flow<PagingData<Article>> {
        val pagingSourceFactory =
            { database.articleDao().getNewsByLanguageAndCategory(language, category) }
        return Pager(
            config = PagingConfig(NETWORK_PAGE_SIZE, maxSize = 300, enablePlaceholders = true),
            remoteMediator = TopArticleRemoteMediator(language, category, service, database),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    @ExperimentalPagingApi
    fun fetchAllArticles(keyword: String, from: String, to: String): Flow<PagingData<Article>> {
        val pagingSourceFactory =
            { database.articleDao().getNewsByKeywordAndFromAndTo(keyword, from, to) }
        return Pager(
            config = PagingConfig(NETWORK_PAGE_SIZE, maxSize = 300, enablePlaceholders = true),
            remoteMediator = AllArticleRemoteMediator(keyword, from, to, service, database),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getArticle(id: String) = flow {
        val article = database.articleDao().getNewsById(id)
        emit(article)
    }.flowOn(Dispatchers.Default)

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }
}
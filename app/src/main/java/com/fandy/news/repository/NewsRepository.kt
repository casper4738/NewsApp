package com.fandy.news.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.fandy.news.api.NewsService
import com.fandy.news.db.NewsDatabase
import com.fandy.news.model.Article
import com.fandy.news.model.ArticleHome
import com.fandy.news.model.ArticleSearch
import com.fandy.news.model.ArticleTopHeadlines
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
    fun fetchTopHeadlinesArticles(language: String, category: String): Flow<PagingData<ArticleTopHeadlines>> {
        val pagingSourceFactory =
            { database.articleDao().getTopHeadlineArticle(language, category) }
        return Pager(
            config = PagingConfig(NETWORK_PAGE_SIZE, maxSize = 300, enablePlaceholders = true),
            remoteMediator = TopHeadlineArticleRemoteMediator(language, category, service, database),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getTopHeadlineArticleById(id: String) = flow {
        val article = database.articleDao().getTopHeadlineArticleById(id)
        emit(article)
    }.flowOn(Dispatchers.Default)

    @ExperimentalPagingApi
    fun fetchEverythingArticles(keyword: String, language: String): Flow<PagingData<ArticleHome>> {
        val pagingSourceFactory =
            { database.articleDao().getHomeArticle(keyword, language) }
        return Pager(
            config = PagingConfig(NETWORK_PAGE_SIZE, maxSize = 300, enablePlaceholders = true),
            remoteMediator = HomeArticleRemoteMediator(keyword, language, service, database),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getHomeArticleById(id: String) = flow {
        val article = database.articleDao().getHomeArticleById(id)
        emit(article)
    }.flowOn(Dispatchers.Default)

    @ExperimentalPagingApi
    fun searchArticles(keyword: String, language: String): Flow<PagingData<ArticleSearch>> {
        val pagingSourceFactory =
            { database.articleDao().searchArticle(keyword, language) }
        return Pager(
            config = PagingConfig(NETWORK_PAGE_SIZE, maxSize = 300, enablePlaceholders = true),
            remoteMediator = SearchArticleRemoteMediator(keyword, language, service, database),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}
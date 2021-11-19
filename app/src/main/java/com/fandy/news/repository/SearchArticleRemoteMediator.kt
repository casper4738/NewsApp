package com.fandy.news.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.fandy.news.api.*
import com.fandy.news.api.asHomeArticleModel
import com.fandy.news.db.NewsDatabase
import com.fandy.news.db.RemoteKey
import com.fandy.news.model.Article
import com.fandy.news.model.ArticleHome
import com.fandy.news.model.ArticleSearch
import com.fandy.news.model.ArticleTopHeadlines
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.io.InvalidObjectException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * RemoteMediator for a DB + Network based PagingData stream, which
 * triggers network requests to fetch additional items when a user
 * scrolls to the end of the list of items stored in DB.
 */
@ExperimentalPagingApi
@Singleton
class SearchArticleRemoteMediator @Inject constructor(
    private val keyword: String,
    private val language: String,
    private val service: NewsService,
    private val database: NewsDatabase
) : RemoteMediator<Int, ArticleSearch>() {

    private val typeArticle = "SEARCH"
    private val remoteKeyDao = database.remoteKeyDao()
    private val articleDao = database.articleDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleSearch>
    ): MediatorResult {
        try {
            val loadKey: Int = when (loadType) {
                LoadType.REFRESH -> {
                    Timber.i("REFRESH")
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE
                }

                LoadType.PREPEND -> {
                    Timber.i("PREPEND")
                    val remoteKey = getRemoteKeyForFirstItem(state)
                        ?: throw InvalidObjectException("Something went wrong.")
                    remoteKey.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    remoteKey.prevKey
                }

                LoadType.APPEND -> {
                    Timber.i("APPEND")

                    val remoteKey = getRemoteKeyForLastItem(state)
                    if (remoteKey?.nextKey == null)
                        return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                    remoteKey.nextKey
                }
            }

            // Suspending network load via Retrofit. This doesn't need to
            // be wrapped in a withContext(Dispatcher.IO) { ... } block
            // since Retrofit's Coroutine CallAdapter dispatches on a
            // worker thread.
            val apiResponse = newsResponse(loadKey, state)
            val news = apiResponse.asSearchArticleModel()
            val endOfPaginationReached = news.isEmpty()

            // Store loaded data, and next key in transaction, so that
            // they're always consistent.
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearRemoteKeys(typeArticle)
                    articleDao.clearHomeArticle()
                }

                val prevKey = if (loadKey == STARTING_PAGE) null else loadKey - 1
                val nextKey = if (endOfPaginationReached) null else loadKey + 1
                val keys = news.map { article ->
                    RemoteKey(
                        articleId = article.id,
                        nextKey = nextKey,
                        prevKey = prevKey,
                        typeArticle = typeArticle
                    )
                }

                for (article in news) {
                    article.keyword = keyword
                    article.language = language
                }

                articleDao.insertSearchArticleAll(news)
                remoteKeyDao.insertAll(keys)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun newsResponse(
        loadKey: Int,
        state: PagingState<Int, ArticleSearch>
    ): NewsResponse {
        return service.getEverthingArticles(
            keyword = keyword,
            language = language,
            page = loadKey,
            pageSize = state.config.pageSize
        )
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ArticleSearch>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { articleId ->
                remoteKeyDao.remoteKeyByArticle(articleId, typeArticle)
            }
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ArticleSearch>): RemoteKey? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { lastArticle ->
            remoteKeyDao.remoteKeyByArticle(lastArticle.id, typeArticle)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ArticleSearch>): RemoteKey? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { firstArticle ->
            remoteKeyDao.remoteKeyByArticle(firstArticle.id, typeArticle)
        }
    }

    companion object {
        private const val STARTING_PAGE = 1
    }
}
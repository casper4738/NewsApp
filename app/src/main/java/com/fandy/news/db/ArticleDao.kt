package com.fandy.news.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fandy.news.model.Article
import com.fandy.news.model.ArticleHome
import com.fandy.news.model.ArticleSearch
import com.fandy.news.model.ArticleTopHeadlines

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopHeadlineArticleAll(news: List<ArticleTopHeadlines>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHomeArticleAll(news: List<ArticleHome>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchArticleAll(news: List<ArticleSearch>)

    @Query("SELECT * FROM article_top_headlines WHERE language = :language AND category = :category")
    fun getTopHeadlineArticle(language: String, category: String): PagingSource<Int, ArticleTopHeadlines>

    @Query("SELECT * FROM article_home WHERE keyword = :keyword AND language = :language ")
    fun getHomeArticle(keyword: String, language: String): PagingSource<Int, ArticleHome>

    @Query("SELECT * FROM article_search WHERE keyword = :keyword AND language = :language ")
    fun searchArticle(keyword: String, language: String): PagingSource<Int, ArticleSearch>

    @Query("DELETE FROM article_top_headlines")
    suspend fun clearTopHeadlineArticle()

    @Query("DELETE FROM article_home")
    suspend fun clearHomeArticle()

    @Query("DELETE FROM article_search")
    suspend fun clearSearchArticle()

    @Query("SELECT * FROM article_top_headlines WHERE id = :id")
    suspend fun getTopHeadlineArticleById(id: String): ArticleTopHeadlines

    @Query("SELECT * FROM article_home WHERE id = :id")
    suspend fun getHomeArticleById(id: String): ArticleHome

    @Query("SELECT * FROM article_search WHERE id = :id")
    suspend fun searchArticleById(id: String): ArticleSearch
}
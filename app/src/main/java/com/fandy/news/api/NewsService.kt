package com.fandy.news.api

import com.fandy.news.util.NEWS_API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * REST API access points. Will fetch a list of news.
 */
interface NewsService {

    @GET("top-headlines")
    suspend fun getTopArticles(
        @Query("language") language: String = "en",
        @Query("country") country: String = "id",
        @Query("apiKey") apiKey: String = NEWS_API_KEY,
        @Query("category") category: String =  "",
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 0
    ): NewsResponse

    @GET("everything")
    suspend fun getEverthingArticles(
        @Query("q") keyword: String = "",
        @Query("language") language: String = "en",
        @Query("apiKey") apiKey: String = NEWS_API_KEY,
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 0
    ): NewsResponse
}
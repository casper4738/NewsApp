package com.fandy.news.api

import com.fandy.news.util.*
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * REST API access points. Will fetch a list of news.
 */
interface NewsService {

    @GET("top-headlines")
    suspend fun getTopArticles(
        @Query("language") language: String = "",
        @Query("country") country: String = "id",
        @Query("apiKey") apiKey: String = NEWS_API_KEY,
        @Query("category") category: String =  "",
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 0
    ): NewsResponse

    @GET("everything")
    suspend fun getAllArticles(
        @Query("q") keyword: String = "",
        @Query("apiKey") apiKey: String = NEWS_API_KEY,
        @Query("from") from: String =  "",
        @Query("to") to: String =  "",
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 0
    ): NewsResponse
}
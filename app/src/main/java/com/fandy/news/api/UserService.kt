package com.fandy.news.api

import com.fandy.news.util.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("top-headlines")
    suspend fun doLogin(
        @Query("country") country: String = "",
        @Query("apiKey") apiKey: String = API_KEY,
    ): UserResponse
}
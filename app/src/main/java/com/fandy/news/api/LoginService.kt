package com.fandy.news.api

import com.fandy.news.model.LoginResponse
import com.fandy.news.util.API_KEY
import com.fandy.news.util.CONTENT_TYPE_JSON
import com.fandy.news.util.ONE_API_KEY
import retrofit2.Response
import retrofit2.http.*

interface LoginService {
    @Headers(
        "X-API-KEY: ${ONE_API_KEY}",
        "Content-Type: application/x-www-form-urlencoded"
    )
    @POST("user/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String = "",
        @Field("password") password: String = "",
    ): Response<LoginResponse>
}
package com.fandy.news.repository

import com.fandy.news.api.LoginService
import com.fandy.news.model.LoginRequest
import com.fandy.news.model.LoginResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val service: LoginService
) {

    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
       return service.login(loginRequest.email, loginRequest.password)
    }

}
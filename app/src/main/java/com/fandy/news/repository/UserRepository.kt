package com.fandy.news.repository

import com.fandy.news.api.UserService
import com.fandy.news.model.LoginUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
//    private val service: UserService,
//    private val database: UserDatabase
) {

//    fun doLogin(loginUser: LoginUser) {
//        val response = service.doLogin(loginUser)
//        database.userDao().insertUser(response)
//    }
//
//    fun getUser(loginUser: LoginUser) = flow {
//        val user = database.userDao().getUserByLoginUser(loginUser)
//        emit(user)
//    }.flowOn(Dispatchers.Default)
}
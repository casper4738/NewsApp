package com.fandy.news.ui.login

import androidx.lifecycle.*
import com.fandy.news.model.Article
import com.fandy.news.model.LoginUser
import com.fandy.news.repository.UserRepository
import com.fandy.news.util.SingleEvent
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _loginUser = MutableLiveData<LoginUser>()

    val userLiveData: LiveData<Article> = _loginUser.switchMap { loginUser ->
        repository.getUser(loginUser).asLiveData()
    }

    fun login(loginUser: LoginUser) {
        _loginUser.value = loginUser
    }
}
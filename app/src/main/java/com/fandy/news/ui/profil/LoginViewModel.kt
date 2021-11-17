package com.fandy.news.ui.profil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fandy.news.model.LoginRequest
import com.fandy.news.model.LoginUser
import com.fandy.news.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : ViewModel() {

    private val _loginUser = MutableLiveData<LoginUser?>()
    val loginUser: LiveData<LoginUser?>
        get() = _loginUser


    fun login(loginRequest: LoginRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.login(loginRequest)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    var loginResponse = response.body()
                    _loginUser.postValue(
                        loginResponse?.data?.let {
                            LoginUser(
                                lastLogin = it.lastLogin ?: "",
                                dateCreated = it.dateCreated ?: "",
                                avatar = it.avatar ?: "",
                                fullName = it.fullName ?: "",
                                lastActivity = it.lastActivity ?: "",
                                id = it.id ?: "",
                                username = it.username ?: "",
                                email = it.email ?: ""
                            )
                        }

                    )
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }

        _loginUser.value
    }

    private fun onError(message: String) {
    }

}
package com.fandy.news.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fandy.news.model.ErrorState
import com.fandy.news.model.LoginRequest
import com.fandy.news.model.LoginUser
import com.fandy.news.repository.LoginRepository
import com.fandy.news.repository.MyPreference
import com.fandy.news.security.SecurityEncryption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val myPreference: MyPreference
) : ViewModel() {

    private val _errorState = MutableLiveData<ErrorState?>()
    val errorState: LiveData<ErrorState?>
        get() = _errorState

    private val _loginUser = MutableLiveData<LoginUser?>()
    val loginUser: LiveData<LoginUser?>
        get() = _loginUser


    fun login(loginRequest: LoginRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.login(loginRequest)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    var loginResponse = response.body()
                    loginResponse?.data?.let {
                        val loginUser = LoginUser(
                            lastLogin = it.lastLogin ?: "",
                            dateCreated = it.dateCreated ?: "",
                            avatar = it.avatar ?: "",
                            fullName = it.fullName ?: "",
                            lastActivity = it.lastActivity ?: "",
                            id = it.id ?: "",
                            username = it.username ?: "",
                            email = it.email ?: ""
                        )
                        _loginUser.postValue(loginUser)

                        myPreference.setStored("login.username", loginUser.username)
                        myPreference.setStored("login.fullName", loginUser.fullName)
                        myPreference.setStored("login.isSuccess", true)
                    }

                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    private fun onError(message: String) {
        myPreference.setStored("login.isLogin", false)
        _errorState.postValue(
            ErrorState(
                status = true,
                message = message
            )
        )
    }

}
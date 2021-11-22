package com.fandy.news.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fandy.news.model.ErrorState
import com.fandy.news.model.LoginRequest
import com.fandy.news.model.LoginUser
import com.fandy.news.repository.LoginRepository
import com.fandy.news.repository.MyPreference
import com.fandy.news.util.SingleEvent
import com.google.gson.JsonObject
import com.google.gson.JsonParser
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

    private val _errorState = MutableLiveData<SingleEvent<ErrorState?>>()
    val errorState: LiveData<SingleEvent<ErrorState?>>
        get() = _errorState

    private val _loginUser = MutableLiveData<SingleEvent<LoginUser?>>()
    val loginUser: LiveData<SingleEvent<LoginUser?>>
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
                        _loginUser.postValue(SingleEvent(loginUser))

                        myPreference.setStored("login.username", loginUser.username)
                        myPreference.setStored("login.fullName", loginUser.fullName)
                        myPreference.setStored("login.isSuccess", "true")
                        myPreference.setStored("login.lastLogin", loginUser.lastLogin)
                        myPreference.setStored("login.dateCreated", loginUser.dateCreated)
                        myPreference.setStored("login.avatar", loginUser.avatar)
                        myPreference.setStored("login.lastActivity", loginUser.lastActivity)
                        myPreference.setStored("login.id", loginUser.id)
                        myPreference.setStored("login.email", loginUser.email)
                    }

                } else {
                    var responseError = response.errorBody()?.string()
                    val jsonObject: JsonObject = JsonParser().parse(responseError).getAsJsonObject()
                    onError(jsonObject.get("message").asString)
                }
            }
        }
    }

    fun isLogin(): Boolean {
        return myPreference.getStoredBoolean("login.isSuccess")
    }

    private fun onError(message: String) {
        myPreference.setStored("login.isLogin", false)
        _errorState.postValue(
            SingleEvent(ErrorState(
                status = true,
                message = message
            ))
        )
    }

}
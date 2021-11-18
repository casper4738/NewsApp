package com.fandy.news.ui.profil

import androidx.lifecycle.*
import com.fandy.news.model.LoginUser
import com.fandy.news.repository.MyPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val myPreference: MyPreference
) : ViewModel() {

    fun isLogin(): Boolean {
        return myPreference.getStoredBoolean("login.isSuccess")
    }

    fun getLoginUser(): LoginUser {
        /* Mencoba Apply SharedPreference With Encrypt & Decrypt Data*/
        return LoginUser(
            lastLogin = myPreference.getStoredString("login.lastLogin"),
            dateCreated = myPreference.getStoredString("login.dateCreated"),
            avatar = myPreference.getStoredString("login.avatar"),
            fullName = myPreference.getStoredString("login.fullName"),
            lastActivity = myPreference.getStoredString("login.lastActivity"),
            id = myPreference.getStoredString("login.id"),
            username = myPreference.getStoredString("login.username"),
            email = myPreference.getStoredString("login.email")
        )
    }

}
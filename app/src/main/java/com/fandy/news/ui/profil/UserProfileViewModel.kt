package com.fandy.news.ui.profil

import androidx.lifecycle.*
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

}
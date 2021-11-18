package com.fandy.news

import android.app.Application
import com.fandy.news.repository.MyPreference
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timerTask

@HiltAndroidApp
class NewsApp() : Application() {

    @Inject
    lateinit var myPreference: MyPreference

    private val applicationScope = CoroutineScope(Dispatchers.Default)
    private lateinit var logoutListener: SessionListener
    private var timer: Timer? = null

    override fun onCreate() {
        super.onCreate()

        applicationScope.launch {
            Timber.plant(Timber.DebugTree())
        }

    }

    fun startUserSession() {
        if (myPreference.getStoredBoolean("login.isSuccess")) {
            cancelTimer()

            val sessionTimeoutInMilis =
                applicationContext.resources.getInteger(R.integer.session_timeout_in_milis).toLong()
            timer = Timer();
            timer!!.schedule(timerTask {
                logoutListener.onSessionLogout()
            }, sessionTimeoutInMilis)
        }

    }

    private fun cancelTimer() {
        timer?.cancel()
    }

    fun registerSessionListener(logoutListener: SessionListener) {
        this.logoutListener = logoutListener
    }

    fun onUserInteraction() {
        startUserSession()
    }

    fun removeSession() {
        cancelTimer()
        myPreference.clearAll()
    }

}
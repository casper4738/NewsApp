package com.fandy.news

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.concurrent.timerTask

@HiltAndroidApp
class NewsApp() : Application() {

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
        cancelTimer()

        println("FANNN startUserSession ${Date()}")
        timer = Timer();
        timer!!.schedule(timerTask {
            println("FANNN startUserSession onSessionLogout ${Date()}")
            logoutListener.onSessionLogout()
        }, 5000)
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

}
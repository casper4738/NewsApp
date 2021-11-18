package com.fandy.news.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.fandy.news.NewsApp
import com.fandy.news.R
import com.fandy.news.SessionListener
import com.fandy.news.databinding.ActivityMainBinding
import com.fandy.news.repository.MyPreference
import com.fandy.news.util.PREF_MODE_KEY
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SessionListener{

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment? ?: return
        navController = host.navController


        binding.bottomNavigation.setupWithNavController(navController)

        (application as NewsApp).registerSessionListener(this)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        (application as NewsApp).onUserInteraction()
    }

    fun logout() {
        (application as NewsApp).removeSession()
    }

    override fun onSessionLogout() {
        runOnUiThread(Runnable {

            MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.session_timeout_title))
                .setMessage(resources.getString(R.string.session_timeout))
                .setCancelable(true)
                .setPositiveButton(resources.getString(R.string.dialog_ok)) { dialog, which ->
                    dialog.dismiss()
                }
                .show()

            (application as NewsApp).removeSession()
            navController.navigate(R.id.homeFragment)

        })
    }

}
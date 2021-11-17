package com.fandy.news.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.fandy.news.R
import com.fandy.news.databinding.ActivityMainBinding
import com.fandy.news.util.PREF_MODE_KEY
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Ensures that the SharedPreferences file is properly initialized with
         * the default values when this method is called for the first time.
         */
//        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false)
        setupMode()

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment? ?: return
        val navController = host.navController

        //Initialize the bottom navigation view
        //create bottom navigation view object
        binding.bottomNavigation.setupWithNavController(navController)
    }


    /**
     * Get the user's mode settings from SharedPreferences.
     */
    private fun setupMode() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val isNightMode = sharedPreferences.getBoolean(PREF_MODE_KEY, false)
        setDefaultNightMode(if (isNightMode) MODE_NIGHT_YES else MODE_NIGHT_NO)
    }

}
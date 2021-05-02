package com.team8.moviecatalog

import android.app.PendingIntent.getActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.team8.moviecatalog.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navView: BottomNavigationView
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        navView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_movie,
                R.id.navigation_anime,
                R.id.navigation_favorite
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val settingActivity = SettingActivity()
        changeLanguage(settingActivity.getDefaultLanguage("country_code", this).toString())

        val notification = (intent.getStringExtra("notificationMessage"))
        if(!notification.isNullOrEmpty())
            navController.navigate(R.id.navigation_favorite)
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        else{
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT).show()
        }

        val handler = Handler()
        handler.postDelayed(Runnable { // Do something after 5s = 5000ms
            doubleBackToExitPressedOnce = false
        }, 1500)
    }

    fun changeLanguage(code: String) {
        val config = resources.configuration
        val locale = Locale(code)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
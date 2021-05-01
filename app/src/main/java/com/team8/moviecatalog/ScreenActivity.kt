package com.team8.moviecatalog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import com.team8.moviecatalog.databinding.ActivityScreenBinding
import java.util.*

class ScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val settingActivity = SettingActivity()
        if(settingActivity.getDefaults("isDarkMode", this) == true)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        changeLanguage(settingActivity.getDefaultLanguage("country_code", this).toString())

        val version = resources.getString(R.string.version) + " " + BuildConfig.VERSION_NAME
        binding.tvScreenVersion.text = version

        Handler(Looper.getMainLooper()).postDelayed({
            val loginIntent = Intent(this, MainActivity::class.java)
            startActivity(loginIntent)
            finish()
        }, 3000)
    }

    fun changeLanguage(code: String) {
        val config = resources.configuration
        val locale = Locale(code)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
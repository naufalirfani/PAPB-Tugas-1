package com.team8.moviecatalog

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        if(getDefaults("isDarkMode", this) == true)
            switch_dark_mode.isChecked = true

        switch_dark_mode.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked)
                setDefaults("isDarkMode", true, this)
            else
                setDefaults("isDarkMode", false, this)

            val screenIntent = Intent(this, ScreenActivity::class.java)
            startActivity(screenIntent)
            finish()
        }
        themeSetting(getString(R.string.setting))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun themeSetting(title: String){
        if(getDefaults("isDarkMode", this) == true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            supportActionBar?.setBackgroundDrawable(ColorDrawable(
                ResourcesCompat.getColor(resources, R.color.dark_actionbar, null)
            ))
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white)

            val html = "<font color=\"#FFFFFF\">$title</font>"
            supportActionBar?.title = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            supportActionBar?.setBackgroundDrawable(ColorDrawable(
                ResourcesCompat.getColor(resources, R.color.white, null)
            ))
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_dark)

            val html = "<font color=\"#202225\">$title</font>"
            supportActionBar?.title = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        supportActionBar?.elevation = 12F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun setDefaults(
        key: String?,
        value: Boolean,
        context: Context?
    ) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getDefaults(key: String?, context: Context?): Boolean? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getBoolean(key, false)
    }
}
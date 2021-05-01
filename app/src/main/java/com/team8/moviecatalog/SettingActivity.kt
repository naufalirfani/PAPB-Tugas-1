package com.team8.moviecatalog

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.preference.PreferenceManager
import com.team8.moviecatalog.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(getDefaults("isDarkMode", this) == true)
            binding.switchDarkMode.isChecked = true

        binding.switchDarkMode.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked)
                setDefaults("isDarkMode", true, this)
            else
                setDefaults("isDarkMode", false, this)

            val screenIntent = Intent(this, ScreenActivity::class.java)
            startActivity(screenIntent)
            finish()
        }
        themeSetting(getString(R.string.setting), this, supportActionBar, resources)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun themeSetting(title: String, context: Context?, actionBar: ActionBar?, resources: Resources){
        if(getDefaults("isDarkMode", context) == true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            actionBar?.setBackgroundDrawable(ColorDrawable(
                ResourcesCompat.getColor(resources, R.color.dark_actionbar, null)
            ))
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white)

            val html = "<font color=\"#FFFFFF\">$title</font>"
            actionBar?.title = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            actionBar?.setBackgroundDrawable(ColorDrawable(
                ResourcesCompat.getColor(resources, R.color.white, null)
            ))
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_dark)

            val html = "<font color=\"#202225\">$title</font>"
            actionBar?.title = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        actionBar?.elevation = 12F
        actionBar?.setDisplayHomeAsUpEnabled(true)
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
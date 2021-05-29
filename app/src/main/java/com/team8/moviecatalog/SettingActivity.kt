package com.team8.moviecatalog

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.preference.PreferenceManager
import com.team8.moviecatalog.adapter.SpinnerAdapter
import com.team8.moviecatalog.databinding.ActivitySettingBinding
import com.team8.moviecatalog.models.Language
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("ClickableViewAccessibility")
class SettingActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivitySettingBinding
    private val languageList: ArrayList<Language> = arrayListOf(
        Language(R.drawable.ic_us, "English", "en"),
        Language(R.drawable.ic_id, "Indonesia", "in")
    )
    private var isTouched: Boolean = false

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

            recreate()
        }
        themeSetting(getString(R.string.setting), this, supportActionBar, resources)

        setSpinner()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setSpinner(){
        binding.spinner.adapter = SpinnerAdapter(this, languageList)

        val index = languageList.indexOfFirst{
            it.code == getDefaultLanguage("country_code", this)
        }
        binding.spinner.setSelection(index)

        binding.spinner.setOnTouchListener{ _, _ ->
            isTouched = true
            false
        }
        binding.spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (!isTouched) return
        val language = languageList[p2]
        setDefaultLanguage("country_code", language.code, this)
        isTouched = false
        val screenIntent = Intent(this, ScreenActivity::class.java)
        startActivity(screenIntent)
        finish()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
//        Nothing selected event
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

    fun setDefaultLanguage(
        key: String?,
        value: String,
        context: Context?
    ) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
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

    fun getDefaultLanguage(key: String?, context: Context?): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(key, "en")
    }
}
package com.example.navigation

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.example.navigation.adapter.PagerAdapter
import com.example.navigation.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "ViewPager and TabLayout"

        binding.mainTab.setTabTextColors(Color.parseColor("#BDBDBD"), Color.parseColor("#000000"))

        val pagerAdapter = PagerAdapter(supportFragmentManager)
        binding.pager.adapter = pagerAdapter
        binding.mainTab.setupWithViewPager(binding.pager)
    }
}
package com.example.navigation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.navigation.ui.beranda.FragmentBeranda
import com.example.navigation.ui.kategori.FragmentKategori
import com.example.navigation.ui.status.FragmentStatus

@Suppress("DEPRECATION")
class PagerAdapter (fm: FragmentManager) : FragmentStatePagerAdapter(fm){

    private val tabName : Array<String> = arrayOf("Beranda", "Kategori", "Status")

    override fun getItem(position: Int): Fragment {
        return when (position) {

            0 -> FragmentBeranda()
            1 -> FragmentKategori()
            else -> FragmentStatus()
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? = tabName[position]
}
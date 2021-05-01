package com.team8.moviecatalog.ui.favorite

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.team8.moviecatalog.R
import com.team8.moviecatalog.adapter.PagerAdapter
import com.team8.moviecatalog.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        context?.let { setupViewPager() }
    }

    private fun setupViewPager() {
        val sectionsPagerAdapter = PagerAdapter(childFragmentManager)
        binding.favoriteViewPager.adapter = sectionsPagerAdapter
        binding.favoriteTabLayout.setupWithViewPager(binding.favoriteViewPager)
        binding.favoriteTabLayout.setTabTextColors(Color.parseColor("#A6A6A6"), Color.parseColor("#FFFFFF"))
    }
}
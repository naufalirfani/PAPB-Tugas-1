package com.team8.moviecatalog.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.team8.moviecatalog.ui.favorite.anime.FavoriteAnimeFragment
import com.team8.moviecatalog.ui.favorite.movie.FavoriteMovieFragment

@Suppress("DEPRECATION")
class PagerAdapter (fm: FragmentManager) : FragmentStatePagerAdapter(fm){

    private val tabName : Array<String> = arrayOf("Movie", "Anime")

    override fun getItem(position: Int): Fragment {
        return when (position) {

            0 -> FavoriteMovieFragment()
            else -> FavoriteAnimeFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? = tabName[position]
}
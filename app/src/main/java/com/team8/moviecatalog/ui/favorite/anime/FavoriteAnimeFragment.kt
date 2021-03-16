package com.team8.moviecatalog.ui.favorite.anime

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.team8.moviecatalog.R
import kotlinx.android.synthetic.main.empty_state.*
import kotlinx.android.synthetic.main.fragment_favorite_anime.*

class FavoriteAnimeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_anime, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enableEmptyStateEmptyFavoriteAnime()
    }

    private fun enableEmptyStateEmptyFavoriteAnime() {
        img_empty_state.setImageResource(R.drawable.ic_empty_state_favorite)
        img_empty_state.contentDescription =
            resources.getString(R.string.empty_state_desc_no_favorite_item_anime)
        title_empty_state.text = resources.getString(R.string.empty_state_title_no_favorite_item)
        desc_empty_state.text =
            resources.getString(R.string.empty_state_desc_no_favorite_item_anime)
        favorite_movie_empty_state.visibility = View.VISIBLE
    }
}
package com.team8.moviecatalog.ui.favorite.anime

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.team8.moviecatalog.R
import com.team8.moviecatalog.adapter.FavoriteAnimeAdapter
import com.team8.moviecatalog.database.AppDatabase
import com.team8.moviecatalog.database.anime.AnimeEntity
import com.team8.moviecatalog.database.movie.MovieEntity
import kotlinx.android.synthetic.main.empty_state.*
import kotlinx.android.synthetic.main.fragment_favorite_anime.*
import kotlinx.android.synthetic.main.fragment_favorite_anime.favorite_movie_empty_state
import java.util.ArrayList

class FavoriteAnimeFragment : Fragment() {

    private lateinit var db: AppDatabase
    private lateinit var favoriteAnimeAdapter: FavoriteAnimeAdapter
    private lateinit var ctx: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_favorite_anime, container, false)
        ctx = this.requireContext()
        db = Room.databaseBuilder(
                ctx,
                AppDatabase::class.java, "favorite"
        ).build()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_favorite_anime.setHasFixedSize(true)
        favoriteAnimeAdapter = FavoriteAnimeAdapter(ctx, db)
        favoriteAnimeAdapter.notifyDataSetChanged()
        rv_favorite_anime.layoutManager = LinearLayoutManager(context)
        rv_favorite_anime.adapter = favoriteAnimeAdapter

        db.animeDao().getAll().observe({ lifecycle }, { animeList ->
            if (animeList.isNotEmpty()) {
                val animeArray = ArrayList<AnimeEntity>()
                animeList.forEach{animeArray.add(it)}
                favorite_anime_progressbar.visibility = View.GONE
                favoriteAnimeAdapter.setData(animeArray)
                favoriteAnimeAdapter.notifyDataSetChanged()
            }
            else{
                favorite_anime_progressbar.visibility = View.GONE
                enableEmptyStateEmptyFavoriteAnime()
            }
        })
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
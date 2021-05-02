package com.team8.moviecatalog.ui.favorite.movie

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.team8.moviecatalog.R
import com.team8.moviecatalog.adapter.FavoriteAdapter
import com.team8.moviecatalog.database.movie.AppMovieDatabase
import kotlinx.android.synthetic.main.empty_state.*
import kotlinx.android.synthetic.main.fragment_favorite_movie.*


class FavoriteMovieFragment : Fragment() {

    private lateinit var db: AppMovieDatabase
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var ctx: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_favorite_movie, container, false)
        ctx = this.requireContext()
        db = Room.databaseBuilder(
            ctx,
            AppMovieDatabase::class.java, "favorite"
        ).build()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_favorite_movie.setHasFixedSize(true)
        favoriteAdapter = FavoriteAdapter(ctx, db)
        favoriteAdapter.notifyDataSetChanged()
        rv_favorite_movie.layoutManager = LinearLayoutManager(context)
        rv_favorite_movie.adapter = favoriteAdapter

        db.movieDao().getAll().observe({ lifecycle }, { movieList ->
            if (movieList.isNotEmpty()) {
                favorite_movie_progressbar.visibility = View.GONE
                favoriteAdapter.setData(movieList)
            }
            else{
                favorite_movie_progressbar.visibility = View.GONE
                enableEmptyStateEmptyFavoriteMovie()
            }
        })
    }

    private fun enableEmptyStateEmptyFavoriteMovie() {
        img_empty_state.setImageResource(R.drawable.ic_empty_state_favorite)
        img_empty_state.contentDescription =
            resources.getString(R.string.empty_state_desc_no_favorite_item_movie)
        title_empty_state.text = resources.getString(R.string.empty_state_title_no_favorite_item)
        desc_empty_state.text =
            resources.getString(R.string.empty_state_desc_no_favorite_item_movie)
        favorite_movie_empty_state.visibility = View.VISIBLE
    }

}
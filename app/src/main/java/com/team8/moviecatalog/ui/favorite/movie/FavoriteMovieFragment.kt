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
import com.team8.moviecatalog.adapter.FavoriteMovieAdapter
import com.team8.moviecatalog.database.AppDatabase
import com.team8.moviecatalog.database.movie.MovieEntity
import kotlinx.android.synthetic.main.empty_state.*
import kotlinx.android.synthetic.main.fragment_favorite_movie.*
import java.util.ArrayList


class FavoriteMovieFragment : Fragment() {

    private lateinit var db: AppDatabase
    private lateinit var favoriteMovieAdapter: FavoriteMovieAdapter
    private lateinit var ctx: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_favorite_movie, container, false)
        ctx = this.requireContext()
        db = Room.databaseBuilder(
            ctx,
            AppDatabase::class.java, "favorite"
        ).build()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_favorite_movie.setHasFixedSize(true)
        favoriteMovieAdapter = FavoriteMovieAdapter(ctx, db)
        favoriteMovieAdapter.notifyDataSetChanged()
        rv_favorite_movie.layoutManager = LinearLayoutManager(context)
        rv_favorite_movie.adapter = favoriteMovieAdapter

        db.movieDao().getAll().observe({ lifecycle }, { movieList ->
            if (movieList.isNotEmpty()) {
                val movieArray = ArrayList<MovieEntity>()
                movieList.forEach{movieArray.add(it)}
                favorite_movie_progressbar.visibility = View.GONE
                favoriteMovieAdapter.setData(movieArray)
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
package com.team8.moviecatalog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.team8.moviecatalog.adapter.GenreAdapter
import com.team8.moviecatalog.adapter.MovieByGenreAdapter
import com.team8.moviecatalog.models.movie.ResultItem
import com.team8.moviecatalog.ui.movie.MovieViewModel
import kotlinx.android.synthetic.main.activity_genre.*
import kotlinx.android.synthetic.main.fragment_movie_content.*
import java.util.*
import kotlin.collections.ArrayList

class GenreActivity : AppCompatActivity() {

    private lateinit var movieViewModel: MovieViewModel
    private var genre: String? = null
    private val settingActivity = SettingActivity()
    private var arrayMovieByGenre = ArrayList<ResultItem?>()
    private lateinit var movieByGenreAdapter: MovieByGenreAdapter
    private val page: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre)

        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        genre = intent.getStringExtra("genre")
        settingActivity.themeSetting(genre.toString(), this, supportActionBar, resources)

        movie_genre_shimmer_view.startShimmerAnimation()

        initData()
    }

    private fun initData(){
        movieViewModel.getMovieByGenre(genre?.toLowerCase(Locale.ROOT), page).observe({ lifecycle }, {
            it?.result?.forEach { resultItem ->
                arrayMovieByGenre.add(resultItem)
            }
            setGenre(arrayMovieByGenre)
        })
    }

    private fun setGenre(array: ArrayList<ResultItem?>){
        movie_genre_rv.setHasFixedSize(true)
        movieByGenreAdapter = MovieByGenreAdapter()
        movieByGenreAdapter.notifyDataSetChanged()
        movieByGenreAdapter.setData(array)
        movie_genre_rv.layoutManager = GridLayoutManager(this, 3)
        movie_genre_rv.adapter = movieByGenreAdapter

        movie_genre_shimmer_view.stopShimmerAnimation()
        movie_genre_shimmer_view.visibility = View.GONE
    }
}
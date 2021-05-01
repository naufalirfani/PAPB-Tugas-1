package com.team8.moviecatalog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.team8.moviecatalog.adapter.MovieByAdapter
import com.team8.moviecatalog.api.movie.MovieClient
import com.team8.moviecatalog.databinding.ActivityGenreBinding
import com.team8.moviecatalog.models.movie.Movie
import com.team8.moviecatalog.models.movie.ResultItem
import com.team8.moviecatalog.ui.movie.MovieViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class GenreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenreBinding
    private lateinit var movieViewModel: MovieViewModel
    private var genre: String? = null
    private val settingActivity = SettingActivity()
    private var arrayMovieByGenre = ArrayList<ResultItem?>()
    private var arrayMovieByGenre2 = ArrayList<ResultItem?>()
    private lateinit var movieByAdapter: MovieByAdapter
    private val PAGE_START = 1
    private var currentPage = PAGE_START
    private var isLoading: Boolean = true
    private val movieClient: MovieClient = MovieClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        genre = intent.getStringExtra("genre")
        settingActivity.themeSetting(genre.toString(), this, supportActionBar, resources)

        binding.movieGenreShimmerView.startShimmerAnimation()

        doLoadData()

        initRecycleView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {onBackPressed()}
        }
        return true
    }

    private fun doLoadData(){
        movieViewModel.getMovieByGenre(genre?.toLowerCase(Locale.ROOT), currentPage).observe({ lifecycle }, {
            it?.result?.forEach { resultItem ->
                arrayMovieByGenre.add(resultItem)
            }

            if(arrayMovieByGenre.isNotEmpty()){

                movieByAdapter.setData(arrayMovieByGenre)
                binding.movieGenreShimmerView.stopShimmerAnimation()
                binding.movieGenreShimmerView.visibility = View.GONE
            }
        })
    }

    private fun initRecycleView(){
        binding.movieGenreProgressBar.visibility = View.GONE
        val gridLayoutManager = GridLayoutManager(this, 3)
        binding.movieGenreRv.layoutManager = gridLayoutManager
        movieByAdapter = MovieByAdapter(this)
        binding.movieGenreRv.adapter = movieByAdapter

        binding.movieGenreNestedScroll.setOnScrollChangeListener {
            v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->

//            if (scrollY > oldScrollY) {
//                Log.i("TAG", "Scroll DOWN")
//            }
//            if (scrollY < oldScrollY) {
//                Log.i("TAG", "Scroll UP")
//            }
//
//            if (scrollY == 0) {
//                Log.i("TAG", "TOP SCROLL")
//            }

            val diff: Int = binding.movieGenreNestedScroll
                    .getChildAt(binding.movieGenreNestedScroll.childCount - 1)
                    .bottom - (binding.movieGenreNestedScroll.height + binding.movieGenreNestedScroll.scrollY)
            if (diff == 0){
                Handler(Looper.getMainLooper()).postDelayed({
                    loadNextPage()
                }, 500)
                binding.movieGenreProgressBar.visibility = View.VISIBLE
            }
            else
                binding.movieGenreProgressBar.visibility = View.GONE

        }
    }

    private fun loadNextPage(){
        currentPage += 1
        arrayMovieByGenre2.clear()
        movieClient.getService().getMovieNewUpload(currentPage)
                .enqueue(object : Callback<Movie> {
                    override fun onFailure(call: Call<Movie>, t: Throwable) {
                        Log.i("Errort", t.message.toString())
                    }

                    override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                        for(result in response.body()?.result!!){
                            arrayMovieByGenre2.add(result)
                        }
                        movieByAdapter.addData(arrayMovieByGenre2)
                        binding.movieGenreProgressBar.visibility = View.GONE
                        isLoading = false
                    }

                })
    }

}
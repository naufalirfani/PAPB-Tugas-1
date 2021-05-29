package com.team8.moviecatalog.ui.movie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.team8.moviecatalog.R
import com.team8.moviecatalog.SearchActivity
import com.team8.moviecatalog.SettingActivity
import com.team8.moviecatalog.adapter.GenreAdapter
import com.team8.moviecatalog.adapter.MovieAdapter
import com.team8.moviecatalog.adapter.MovieByAdapter
import com.team8.moviecatalog.adapter.SliderAdapter
import com.team8.moviecatalog.models.movie.ResultItem
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.android.synthetic.main.fragment_movie.view.*
import kotlinx.android.synthetic.main.fragment_movie_content.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class MovieFragment : Fragment() {

    private lateinit var movieViewModel: MovieViewModel
    private var arraySlider = ArrayList<ResultItem?>()
    private var arrayMovie = ArrayList<ResultItem?>()
    private var arrayMovie2 = ArrayList<ResultItem?>()
    private var arrayMovieNewUpload = ArrayList<ResultItem?>()
    private var arrayMovieTitle = ArrayList<String>()
    private var listRandom: MutableList<Int> = mutableListOf()
    private lateinit var genreAdapter: GenreAdapter
    private lateinit var rvAdapter: MovieAdapter
    private lateinit var mShimmerViewContainer: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_movie, container, false)
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        mShimmerViewContainer = root.findViewById(R.id.shimmer_view_container)
        mShimmerViewContainer.startShimmerAnimation()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_for_u.visibility = View.GONE
        tv_new_release.visibility = View.GONE

        movieViewModel.getMovieNewUpload(1).observe({ lifecycle }, {
            it?.result?.forEach { resultItem ->
                arrayMovieNewUpload.add(resultItem)
                arrayMovieTitle.add(resultItem?.title.toString())
            }
            if(arrayMovieNewUpload.isNotEmpty()){
                setSlider(arrayMovieNewUpload)
                setRecycleview(arrayMovieNewUpload)

                tv_for_u.visibility = View.VISIBLE
                tv_new_release.visibility = View.VISIBLE
            }
        })

        btn_setting.setOnClickListener {
            val settingIntent = Intent(context, SettingActivity::class.java)
            context?.startActivity(settingIntent)
        }

        btn_search_movie.setOnClickListener {
            val searchIntent = Intent(context, SearchActivity::class.java)
            searchIntent.putExtra("activity", "movie")
            searchIntent.putStringArrayListExtra("arrayMovieTitle", arrayMovieTitle)
            context?.startActivity(searchIntent)
        }
    }

    private fun setSlider(listSlider: ArrayList<ResultItem?>){

        listRandom.clear()
        while (listRandom.size < 5){
            val angka = Random.nextInt(0, listSlider.size)

            if(listRandom.contains(angka))
                continue

            listRandom.add(angka)

            if (listRandom.size == 5)
                break
        }

        listRandom.forEach{
            arraySlider.add(listSlider[it])
        }

        val sliderView: SliderView? = view?.findViewById(R.id.imageSlider)
        val sliderAdapter = context?.let { SliderAdapter(it) }
        sliderAdapter?.setData(arraySlider)
        if (sliderAdapter != null) {
            sliderView?.setSliderAdapter(sliderAdapter)
        }
        sliderView?.setIndicatorAnimation(IndicatorAnimationType.FILL)
        sliderView?.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView?.isAutoCycle = true
        sliderView?.startAutoCycle()

        mShimmerViewContainer.stopShimmerAnimation()
        mShimmerViewContainer.visibility = View.GONE

        setGenre()
    }

    private fun setGenre(){
        movie_content_rv.setHasFixedSize(true)
        val context: Context = requireContext()
        genreAdapter = GenreAdapter(context)
        genreAdapter.notifyDataSetChanged()
        movie_content_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        movie_content_rv.adapter = genreAdapter
    }

    private fun setRecycleview(listNew: ArrayList<ResultItem?>){
        // set for u
        listRandom.clear()
        while (listRandom.size < 10){
            val angka = Random.nextInt(0, listNew.size)

            if(listRandom.contains(angka))
                continue

            listRandom.add(angka)

            if (listRandom.size == 10)
                break
        }

        listRandom.forEach{
            arrayMovie.add(listNew[it])
        }

        movie_for_u_rv.setHasFixedSize(true)
        val context: Context = requireContext()
        rvAdapter = MovieAdapter(context)
        rvAdapter.notifyDataSetChanged()
        rvAdapter.setData(arrayMovie)
        movie_for_u_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        movie_for_u_rv.adapter = rvAdapter

        // set new release
        var i = 0
        while (i < 10){
            arrayMovie2.add(listNew[i])
            if (i == 10)
                break
            i++
        }

        movie_new_rv.setHasFixedSize(true)
        rvAdapter = MovieAdapter(context)
        rvAdapter.notifyDataSetChanged()
        rvAdapter.setData(arrayMovie2)
        movie_new_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        movie_new_rv.adapter = rvAdapter
    }
}
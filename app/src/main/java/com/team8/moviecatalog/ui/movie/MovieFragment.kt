package com.team8.moviecatalog.ui.movie

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
import com.team8.moviecatalog.SettingActivity
import com.team8.moviecatalog.adapter.GenreAdapter
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
    private var arrayMovieNewUpload = ArrayList<ResultItem?>()
    private var listRandom: MutableList<Int> = mutableListOf()
    private lateinit var genreAdapter: GenreAdapter
    private lateinit var mShimmerViewContainer: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_movie, container, false)
        root.btn_setting.setOnClickListener {
            Toast.makeText(context, "dasdsa", Toast.LENGTH_SHORT).show()
        }
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        mShimmerViewContainer = root.findViewById(R.id.shimmer_view_container)
        mShimmerViewContainer.startShimmerAnimation()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel.getMovieNewUpload(2).observe({ lifecycle }, {
            it?.result?.forEach { resultItem ->
                arrayMovieNewUpload.add(resultItem)
            }
            setSlider(arrayMovieNewUpload)
            setGenre()
        })

        btn_setting.setOnClickListener {
            val settingIntent = Intent(context, SettingActivity::class.java)
            context?.startActivity(settingIntent)
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

    }

    private fun setGenre(){
        movie_content_rv.setHasFixedSize(true)
        genreAdapter = GenreAdapter()
        genreAdapter.notifyDataSetChanged()
        movie_content_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        movie_content_rv.adapter = genreAdapter
    }
}
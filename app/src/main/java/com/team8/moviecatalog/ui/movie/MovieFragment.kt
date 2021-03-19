package com.team8.moviecatalog.ui.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.team8.moviecatalog.R
import com.team8.moviecatalog.adapter.GenreAdapter
import com.team8.moviecatalog.adapter.SliderAdapter
import com.team8.moviecatalog.models.movie.ResultItem
import kotlinx.android.synthetic.main.fragment_movie_content.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class MovieFragment : Fragment() {

    private lateinit var movieViewModel: MovieViewModel
    private var arraySlider = ArrayList<ResultItem?>()
    private var arrayMovieNewUpload = ArrayList<ResultItem?>()
    private var listRandom: MutableList<Int> = mutableListOf()
    private lateinit var genreAdapter: GenreAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_movie, container, false)
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel.getMovieNewUpload(null).observe({ lifecycle }, {
            it?.result?.forEach { resultItem ->
                arrayMovieNewUpload.add(resultItem)
            }
            setSlider(arrayMovieNewUpload)
            setGenre()
        })
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
        val sliderAdapter = SliderAdapter()
        sliderAdapter.setData(arraySlider)
        sliderView?.setSliderAdapter(sliderAdapter)
        sliderView?.setIndicatorAnimation(IndicatorAnimationType.FILL)
        sliderView?.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView?.isAutoCycle = true
        sliderView?.startAutoCycle()

    }

    private fun setGenre(){
        movie_content_rv.setHasFixedSize(true)
        genreAdapter = GenreAdapter()
        genreAdapter.notifyDataSetChanged()
        movie_content_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        movie_content_rv.adapter = genreAdapter
    }
}
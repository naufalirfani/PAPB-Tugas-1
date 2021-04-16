package com.team8.moviecatalog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.team8.moviecatalog.databinding.ActivityDetailBinding
import com.team8.moviecatalog.models.movie.ResultItem
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var activity: String? = null
    private var movie: ResultItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = intent.getStringExtra("activity")
        movie = intent.getParcelableExtra("movie")

        supportActionBar?.hide()

        setContent()
    }

    private fun setContent(){
        binding.detailToolbar.title = movie?.title
        tv_detail_name.text = movie?.title
        Glide.with(this).clear(binding.imgDetailHightlight)
        Glide.with(this)
            .load(movie?.thumbnail)
//                .apply(RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888)
//                        .override(Target.SIZE_ORIGINAL))
            .fitCenter()
            .into(binding.imgDetailHightlight)

        Glide.with(this).clear(img_detail_poster)
        Glide.with(this)
            .load(movie?.thumbnail)
                .apply(RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL))
            .fitCenter()
            .into(img_detail_poster)
    }
}
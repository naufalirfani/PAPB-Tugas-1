package com.team8.moviecatalog

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.team8.moviecatalog.database.movie.AppMovieDatabase
import com.team8.moviecatalog.database.movie.MovieEntity
import com.team8.moviecatalog.databinding.ActivityDetailBinding
import com.team8.moviecatalog.models.movie.ResultItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.modelmapper.ModelMapper

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var activity: String? = null
    private var movie: ResultItem? = null
    private lateinit var db: AppMovieDatabase
    private var isFavorite: Boolean = false
    private lateinit var movieFavorite: MovieEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = intent.getStringExtra("activity")
        movie = intent.getParcelableExtra("movie")

        supportActionBar?.hide()

        setContent()

        db = Room.databaseBuilder(
            applicationContext,
            AppMovieDatabase::class.java, "favorite"
        ).build()

        loadIfFavorite()

        movieFavorite = MovieEntity()

        addRemoveFavorite()
    }

    private fun setContent(){
        binding.detailToolbar.title = movie?.title
        binding.detailContent.tvDetailName.text = movie?.title
        Glide.with(this).clear(binding.imgDetailHightlight)
        Glide.with(this)
            .load(movie?.thumbnail)
//                .apply(RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888)
//                        .override(Target.SIZE_ORIGINAL))
            .fitCenter()
            .into(binding.imgDetailHightlight)

        Glide.with(this).clear(binding.detailContent.imgDetailPoster)
        Glide.with(this)
            .load(movie?.thumbnail)
                .apply(RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL))
            .fitCenter()
            .into(binding.detailContent.imgDetailPoster)
    }

    private fun loadIfFavorite(){
        binding.detailMovieProgressbar.visibility = View.VISIBLE
        val user = db.movieDao().loadById(movie?.title.toString())
        user.observe( {lifecycle}, { movieEntity ->
            if (movieEntity != null) {
                isFavorite = true
                binding.fabFavorite.imageTintList = ColorStateList.valueOf(Color.parseColor("#E50C2F"))
                binding.detailMovieProgressbar.visibility = View.GONE
            }
            else{
                binding.fabFavorite.imageTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                binding.detailMovieProgressbar.visibility = View.GONE
            }
        })
    }

    private fun addRemoveFavorite(){
        val modelMapper = ModelMapper()
        movieFavorite = modelMapper.map(movie, MovieEntity::class.java)
        binding.fabFavorite.setOnClickListener {
            if (isFavorite){
                isFavorite = false
                binding.fabFavorite.imageTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                Toast.makeText(this, "Dihapus dari Favorite", Toast.LENGTH_SHORT).show()
                GlobalScope.launch {
                    db.movieDao().deleteMovie(movie?.title.toString())
                }
            }
            else{
                isFavorite = true
                binding.fabFavorite.imageTintList = ColorStateList.valueOf(Color.parseColor("#E50C2F"))
                Toast.makeText(this, "Ditambahkan ke Favorite", Toast.LENGTH_SHORT).show()
                GlobalScope.launch {
                    db.movieDao().insertMovie(movieFavorite)
                }
            }
        }
    }
}
package com.team8.moviecatalog

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.team8.moviecatalog.database.anime.AnimeEntity
import com.team8.moviecatalog.database.AppDatabase
import com.team8.moviecatalog.database.movie.MovieEntity
import com.team8.moviecatalog.databinding.ActivityDetailBinding
import com.team8.moviecatalog.models.anime.AnimeResult
import com.team8.moviecatalog.models.movie.ResultItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.modelmapper.ModelMapper
import java.net.URL

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var activity: String? = null
    private var movie: ResultItem? = null
    private var anime: AnimeResult? = null
    private lateinit var db: AppDatabase
    private var isFavorite: Boolean = false
    private lateinit var movieFavorite: MovieEntity
    private lateinit var animeFavorite: AnimeEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = intent.getStringExtra("activity")
        if(activity == "movie")
            movie = intent.getParcelableExtra("movie")
        else
            anime = intent.getParcelableExtra("anime")

        supportActionBar?.hide()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "favorite"
        ).build()

        movieFavorite = MovieEntity()

        setContent(activity.toString())
        loadIfFavorite(activity.toString())
        addRemoveFavorite(activity.toString())
    }

    private fun setContent(activity: String){
        if(activity == "movie"){
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

            val videoId = movie?.trailer?.replace("https://www.youtube.com/watch?v=", "")
            binding.detailContent.btnDetailTrailer.setOnClickListener {
                val intent = Intent(this, TrailerActivity::class.java)
                intent.putExtra("videoId", videoId)
                startActivity(intent)
            }
        }
        else{
            binding.detailToolbar.title = anime?.title
            binding.detailContent.tvDetailName.text = anime?.title
            binding.detailContent.tvDetailDesc.text = anime?.synopsis
            Glide.with(this).clear(binding.imgDetailHightlight)
            Glide.with(this)
                    .load(anime?.imageUrl)
//                .apply(RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888)
//                        .override(Target.SIZE_ORIGINAL))
                    .fitCenter()
                    .into(binding.imgDetailHightlight)

            Glide.with(this).clear(binding.detailContent.imgDetailPoster)
            Glide.with(this)
                    .load(anime?.imageUrl)
                    .apply(RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888)
                            .override(Target.SIZE_ORIGINAL))
                    .fitCenter()
                    .into(binding.detailContent.imgDetailPoster)

            binding.detailContent.btnDetailTrailer.visibility = View.GONE
        }
    }

    private fun loadIfFavorite(activity: String){
        if(activity == "movie"){
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
        else{
            binding.detailMovieProgressbar.visibility = View.VISIBLE
            val user = db.animeDao().loadById(anime?.title.toString())
            user.observe( {lifecycle}, { animeEntity ->
                if (animeEntity != null) {
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
    }

    private fun addRemoveFavorite(activity: String){
        if(activity == "movie"){
            val modelMapper = ModelMapper()
            movieFavorite = modelMapper.map(movie, MovieEntity::class.java)
            binding.fabFavorite.setOnClickListener {
                if (isFavorite){
                    isFavorite = false
                    binding.fabFavorite.imageTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                    Toast.makeText(this, resources.getString(R.string.delete_from_favorite), Toast.LENGTH_SHORT).show()
                    GlobalScope.launch {
                        db.movieDao().deleteMovie(movie?.title.toString())
                    }
                }
                else{
                    isFavorite = true
                    binding.fabFavorite.imageTintList = ColorStateList.valueOf(Color.parseColor("#E50C2F"))
                    Toast.makeText(this, resources.getString(R.string.added_to_favorite), Toast.LENGTH_SHORT).show()
                    GlobalScope.launch {
                        db.movieDao().insertMovie(movieFavorite)
                        val msg = String.format(resources.getString(R.string.notification_msg, movieFavorite.title))
                        val notifId = 2
                        showNotification(resources.getString(R.string.added_to_favorite), msg, notifId, movieFavorite.thumbnail.toString())
                    }
                }
            }
        }
        else{
            val modelMapper = ModelMapper()
            animeFavorite = modelMapper.map(anime, AnimeEntity::class.java)
            binding.fabFavorite.setOnClickListener {
                if (isFavorite){
                    isFavorite = false
                    binding.fabFavorite.imageTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                    Toast.makeText(this, resources.getString(R.string.delete_from_favorite), Toast.LENGTH_SHORT).show()
                    GlobalScope.launch {
                        db.animeDao().deleteAnime(anime?.title.toString())
                    }
                }
                else{
                    isFavorite = true
                    binding.fabFavorite.imageTintList = ColorStateList.valueOf(Color.parseColor("#E50C2F"))
                    Toast.makeText(this, resources.getString(R.string.added_to_favorite), Toast.LENGTH_SHORT).show()
                    GlobalScope.launch {
                        db.animeDao().insertAnime(animeFavorite)
                        val msg = String.format(resources.getString(R.string.notification_msg, animeFavorite.title))
                        val notifId = 2
                        showNotification(resources.getString(R.string.added_to_favorite), msg, notifId, animeFavorite.imageUrl.toString())
                    }
                }
            }
        }
    }

    private fun showNotification(title: String, message: String, notifId: Int, urlString: String) {

        val CHANNEL_ID = "Channel_01"
        val CHANNEL_NAME = "Movie channel"

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("notificationMessage", "notification")
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingNotificationIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val url = URL(urlString)
        val icon = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movie_logo)
                .setLargeIcon(icon)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setColor(ContextCompat.getColor(applicationContext, android.R.color.transparent))
//                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(icon))
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(alarmSound)
                .setContentIntent(pendingNotificationIntent)
                .setAutoCancel(true)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT)

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(CHANNEL_ID)

            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()

        notificationManager.notify(notifId, notification)

    }
}
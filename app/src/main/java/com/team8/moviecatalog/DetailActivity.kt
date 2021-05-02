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
import com.team8.moviecatalog.database.movie.AppMovieDatabase
import com.team8.moviecatalog.database.movie.MovieEntity
import com.team8.moviecatalog.databinding.ActivityDetailBinding
import com.team8.moviecatalog.models.movie.ResultItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.modelmapper.ModelMapper
import java.net.URL

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
                    showNotification(resources.getString(R.string.added_to_favorite), msg, notifId)
                }
            }
        }
    }

    private fun showNotification(title: String, message: String, notifId: Int) {

        val CHANNEL_ID = "Channel_01"
        val CHANNEL_NAME = "Movie channel"

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("notificationMessage", "notification")
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingNotificationIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val url = URL(movieFavorite.thumbnail)
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
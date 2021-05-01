package com.team8.moviecatalog

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.team8.moviecatalog.databinding.ActivityTrailerBinding


class TrailerActivity : AppCompatActivity(), YouTubePlayerFullScreenListener, YouTubePlayerListener {

    private var currentSec: Float = 0F
    private var videoId: String? = null
    private lateinit var binding: ActivityTrailerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrailerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        videoId = intent.getStringExtra("videoId")
        initYoutubePlayer(videoId.toString())
        binding.youtubePlayerView.addFullScreenListener(this)
    }

    override fun onResume() {
        super.onResume()
        initYoutubePlayer(videoId.toString())
    }

    override fun onBackPressed() {
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            onYouTubePlayerExitFullScreen()
        }
        else{
            super.onBackPressed()
            return
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            hideSystemUI()
        }
        else if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            showSystemUI()
        }
    }

    private fun initYoutubePlayer(videoId: String){

        lifecycle.addObserver(binding.youtubePlayerView)
        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoId, currentSec)
            }
        })
    }

    override fun onYouTubePlayerEnterFullScreen() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        resources.configuration.orientation = Configuration.ORIENTATION_LANDSCAPE
        hideSystemUI()
    }

    override fun onYouTubePlayerExitFullScreen() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        resources.configuration.orientation = Configuration.ORIENTATION_PORTRAIT
        showSystemUI()
    }

    @Suppress("DEPRECATION")
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    @Suppress("DEPRECATION")
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
    }

    override fun onApiChange(youTubePlayer: YouTubePlayer) {
        //api change
    }

    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
        //current second
        currentSec = second
    }

    override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
        //error
    }

    override fun onPlaybackQualityChange(youTubePlayer: YouTubePlayer, playbackQuality: PlayerConstants.PlaybackQuality) {
        //quality change
    }

    override fun onPlaybackRateChange(youTubePlayer: YouTubePlayer, playbackRate: PlayerConstants.PlaybackRate) {
        //rate change
    }

    override fun onReady(youTubePlayer: YouTubePlayer) {
        //ready
    }

    override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
        //state change
    }

    override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {
        //video duration
    }

    override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
        //video id
    }

    override fun onVideoLoadedFraction(youTubePlayer: YouTubePlayer, loadedFraction: Float) {
        //video fraction
    }
}
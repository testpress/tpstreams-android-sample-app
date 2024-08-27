package com.example.sampleplayer

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.sampleplayer.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    private val TAG = "PlayerActivity"
    private val ORG_CODE = "6eafqn"
    private val DRM_VIDEO_ID = "C5cE3UJrscE"
    private val DRM_ACCESS_TOKEN = "e0067d6a-d778-414f-9de4-6a340d88bef6"
    private val NON_DRM_VIDEO_ID = "8DjR3FzHy4Z"
    private val NON_DRM_ACCESS_TOKEN = "0cebd232-3699-4908-81f0-3cc2fa9497f8"
    private val isDRMVideo = true

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    private var player: Player? = null

    private val playbackStateListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            val stateString: String = when (playbackState) {
                ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE"
                ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING"
                ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY"
                ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED"
                else -> "UNKNOWN_STATE"
            }
            Log.d(TAG, "Playback state changed to: $stateString")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(viewBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M || player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            viewBinding.playerView.player = exoPlayer
            exoPlayer.trackSelectionParameters = exoPlayer.trackSelectionParameters
                .buildUpon()
                .setMaxVideoSizeSd()
                .build()
            exoPlayer.setMediaItem(createMediaItem())
            exoPlayer.playWhenReady = true
            exoPlayer.addListener(playbackStateListener)
            exoPlayer.prepare()
        }
    }

    private fun releasePlayer() {
        player?.apply {
            removeListener(playbackStateListener)
            release()
        }
        player = null
    }

    private fun createMediaItem(): MediaItem {
        return MediaItem.Builder()
            .setUri(getPlaybackUrl())
            .apply {
                if (isDRMVideo) {
                    setDrmConfiguration(
                        MediaItem.DrmConfiguration.Builder(C.WIDEVINE_UUID)
                            .setLicenseUri(getDRMLicenseUrl())
                            .build()
                    )
                }
            }
            .build()
    }

    private fun getPlaybackUrl(): String {
        // TODO: Implement API call to fetch asset details and dynamically determine the URL.
        // Sample URL for testing:
        // For DRM video, use DASH format URL
        // For non-DRM video, use HLS format URL

        return if (isDRMVideo) {
            "https://d384padtbeqfgy.cloudfront.net/transcoded/C5cE3UJrscE/video.mpd"
        } else {
            "https://d384padtbeqfgy.cloudfront.net/transcoded/AajXKGG6pKM/video.m3u8"
        }
    }

    private fun getDRMLicenseUrl(): String {
        val videoId = if (isDRMVideo) DRM_VIDEO_ID else NON_DRM_VIDEO_ID
        val accessToken = getAccessToken()
        return "https://app.tpstreams.com/api/v1/$ORG_CODE/assets/$videoId/drm_license/?access_token=$accessToken"
    }

    private fun getAccessToken(): String {
        // TODO: Implement an API call to dynamically retrieve the access token based on the video_id.
        // Use predefined tokens for now
        return if (isDRMVideo) DRM_ACCESS_TOKEN else NON_DRM_ACCESS_TOKEN
    }

}
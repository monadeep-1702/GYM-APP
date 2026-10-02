package com.priyabrata.gymapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.priyabrata.gymapp.R

class MeditationAudioService : Service() {

    private var player: ExoPlayer? = null
    private val channelId = "meditation_channel"
    private val notificationId = 1

    companion object {
        private var playerInstance: ExoPlayer? = null
        fun getPlayer(): ExoPlayer? = playerInstance

        const val ACTION_PLAY = "ACTION_PLAY"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_STOP = "ACTION_STOP"
        const val EXTRA_AUDIO_URL = "EXTRA_AUDIO_URL"
        const val EXTRA_TITLE = "EXTRA_TITLE"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // ⚡ IMMEDIATELY call startForeground to avoid crash
        startForeground(notificationId, buildNotification("Meditation", isPlaying = false))

        when (intent?.action) {
            ACTION_STOP -> {
                stopPlayback()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
                return START_NOT_STICKY
            }
            ACTION_PAUSE -> {
                player?.pause()
                updateNotification(isPlaying = false)
                return START_NOT_STICKY
            }
            ACTION_PLAY -> {
                val url = intent.getStringExtra(EXTRA_AUDIO_URL)
                val title = intent.getStringExtra(EXTRA_TITLE) ?: "Meditation"

                if (url != null) {
                    playAudio(url, title)
                } else {
                    // Resume current track
                    player?.play()
                    updateNotification(isPlaying = true)
                }
                return START_NOT_STICKY
            }
        }

        // Default: play if URL provided
        val url = intent?.getStringExtra(EXTRA_AUDIO_URL)
        val title = intent?.getStringExtra(EXTRA_TITLE) ?: "Meditation"
        if (url != null) {
            playAudio(url, title)
        }

        return START_NOT_STICKY
    }

    private fun playAudio(url: String, title: String) {
        // Release old player
        player?.release()
        playerInstance = null

        player = ExoPlayer.Builder(this).build().also { exo ->
            playerInstance = exo
            val mediaItem = MediaItem.fromUri(url)
            exo.setMediaItem(mediaItem)
            exo.prepare()
            exo.playWhenReady = true

            exo.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    when (state) {
                        Player.STATE_ENDED -> {
                            stopForeground(STOP_FOREGROUND_REMOVE)
                            stopSelf()
                        }
                        Player.STATE_READY -> {
                            updateNotification(isPlaying = exo.isPlaying)
                        }
                    }
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    updateNotification(isPlaying = isPlaying)
                }
            })
        }

        updateNotification(isPlaying = true)
    }

    private fun stopPlayback() {
        player?.stop()
        player?.release()
        player = null
        playerInstance = null
    }

    private fun updateNotification(isPlaying: Boolean) {
        val notification = buildNotification("Meditation", isPlaying)
        val nm = getSystemService(NotificationManager::class.java)
        nm.notify(notificationId, notification)
    }

    private fun buildNotification(title: String, isPlaying: Boolean): Notification {
        val stopIntent = Intent(this, MeditationAudioService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPending = PendingIntent.getService(
            this, 0, stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val playPauseIntent = Intent(this, MeditationAudioService::class.java).apply {
            action = if (isPlaying) ACTION_PAUSE else ACTION_PLAY
        }
        val playPausePending = PendingIntent.getService(
            this, 1, playPauseIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val playPauseIcon = if (isPlaying)
            android.R.drawable.ic_media_pause
        else
            android.R.drawable.ic_media_play

        val playPauseText = if (isPlaying) "Pause" else "Play"

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(if (isPlaying) "Playing..." else "Paused")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .addAction(android.R.drawable.ic_delete, "Stop", stopPending)
            .addAction(playPauseIcon, playPauseText, playPausePending)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Meditation Audio",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Meditation playback controls"
                setSound(null, null)
            }
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        stopPlayback()
        super.onDestroy()
    }
}

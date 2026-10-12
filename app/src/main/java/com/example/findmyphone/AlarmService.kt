package com.example.findmyphone

import android.app.*
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.IBinder
import android.content.Context
import android.media.AudioManager

class AlarmService : Service() {

    private var player: MediaPlayer? = null
    private lateinit var audioManager: AudioManager
    private var originalAlarmVolume: Int? = null

    override fun onCreate() {
        super.onCreate()

        val channel = NotificationChannel(
            "finder_alarm",
            "Phone Finder Alarm",
            NotificationManager.IMPORTANCE_LOW
        )

        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        if (intent?.action == "STOP_ALARM") {
            stopSelf()
            return START_NOT_STICKY
        }

        val notification = Notification.Builder(this, "finder_alarm")
            .setContentTitle("FindMyPhone")
            .setContentText("Phone finder alarm is active")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .build()


        if (android.os.Build.VERSION.SDK_INT >=
            android.os.Build.VERSION_CODES.Q) {

            startForeground(
                1,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )

        } else {

            startForeground(1, notification)

        }


        if (player == null) {
            startAlarm()
        }

        return START_STICKY
    }

    private fun startAlarm() {
        audioManager = getSystemService(
            Context.AUDIO_SERVICE
        ) as AudioManager

        if (originalAlarmVolume == null) {
            originalAlarmVolume = audioManager.getStreamVolume(
                AudioManager.STREAM_ALARM
            )
        }

        val maxVolume = audioManager.getStreamMaxVolume(
            AudioManager.STREAM_ALARM
        )

        audioManager.setStreamVolume(
            AudioManager.STREAM_ALARM,
            maxVolume,
            0
        )

        val alarmUri = RingtoneManager.getDefaultUri(
            RingtoneManager.TYPE_ALARM
        )

        player = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(
                        AudioAttributes.CONTENT_TYPE_SONIFICATION
                    )
                    .build()
            )

            setDataSource(this@AlarmService, alarmUri)
            isLooping = true
            prepare()
            start()
        }
    }

    override fun onDestroy() {
        player?.release()
        player = null

        if (::audioManager.isInitialized) {
            originalAlarmVolume?.let { volume ->
                audioManager.setStreamVolume(
                    AudioManager.STREAM_ALARM,
                    volume,
                    0
                )
            }
        }

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
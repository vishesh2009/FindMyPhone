package com.example.findmyphone


import android.Manifest
import android.content.pm.PackageManager
import android.app.Activity
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.content.Intent
import android.widget.Toast
import android.view.View
class MainActivity : Activity() {

    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkSelfPermission(Manifest.permission.RECEIVE_SMS)
            != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                arrayOf(Manifest.permission.RECEIVE_SMS),
                100
            )
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 100, 50, 50)
        }

        val startButton = Button(this).apply {
            text = "START ALARM"

            setOnClickListener {
                val intent = Intent(
                    this@MainActivity,
                    AlarmService::class.java
                )

                startForegroundService(intent)
            }
        }

        val stopButton = Button(this).apply {
            text = "STOP ALARM"

            setOnClickListener {
                val intent = Intent(
                    this@MainActivity,
                    AlarmService::class.java
                )

                intent.action = "STOP_ALARM"
                startService(intent)
            }
        }

        layout.addView(startButton)
        layout.addView(stopButton)

// Create TEST SMS button
        val testSmsButton = Button(this).apply {
            text = "TEST SMS"
            visibility = if (BuildConfig.DEBUG) View.VISIBLE else View.GONE


            setOnClickListener {

                val accepted = CommandHandler.processCommand(
                    "FINDPHONE TEST"
                )

                if (accepted) {

                    // Start our existing background alarm service
                    val intent = Intent(
                        this@MainActivity,
                        AlarmService::class.java
                    )

                    startForegroundService(intent)

                    Toast.makeText(
                        this@MainActivity,
                        "Command accepted! Starting alarm.",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    Toast.makeText(
                        this@MainActivity,
                        "Invalid SMS command",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

// Add third button
        layout.addView(testSmsButton)

// Display everything
        setContentView(layout)
    }

    private fun startAlarm() {
        stopAlarm()

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

            setDataSource(this@MainActivity, alarmUri)
            isLooping = true
            prepare()
            start()
        }
    }

    private fun stopAlarm() {
        player?.run {
            if (isPlaying) stop()
            release()
        }
        player = null
    }

    override fun onDestroy() {
        stopAlarm()
        super.onDestroy()
    }
}
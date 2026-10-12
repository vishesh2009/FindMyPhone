
package com.example.findmyphone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        Log.e("FindMyPhone", "SMS RECEIVER ACTIVATED: ${intent.action}")
        if (intent.action !=
            Telephony.Sms.Intents.SMS_RECEIVED_ACTION
        ) return

        val messages =
            Telephony.Sms.Intents.getMessagesFromIntent(intent)

        if (messages.isEmpty()) return

        val messageText = messages.joinToString("") {
            it.messageBody ?: ""
        }.trim()

        Log.i("FindMyPhone", "Incoming SMS received")

        val accepted =
            CommandHandler.processCommand(messageText)

        if (accepted) {
            Log.i(
                "FindMyPhone",
                "Command accepted"
            )

            AlarmNotification.show(context)

            try {
                val alarmIntent = Intent(
                    context,
                    AlarmService::class.java
                )

                context.startForegroundService(alarmIntent)

                Log.i(
                    "FindMyPhone",
                    "Alarm service start requested"
                )
            } catch (e: Exception) {
                Log.e(
                    "FindMyPhone",
                    "Alarm service start failed",
                    e
                )
            }
        }
    }
}

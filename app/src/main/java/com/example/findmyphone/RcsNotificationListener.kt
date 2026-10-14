
package com.example.findmyphone

import android.app.Notification
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class RcsNotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        // Only process notifications from Google Messages.
        if (sbn.packageName != "com.google.android.apps.messaging") {
            return
        }

        val notification = sbn.notification

        // Ignore non-message notifications.
        if (notification.category != Notification.CATEGORY_MESSAGE) {
            return
        }

        val extras = notification.extras

        val message = extras.getCharSequence(
            Notification.EXTRA_TEXT
        )?.toString()?.trim() ?: return

        if (!CommandHandler.processCommand(message)) {
            return
        }

        Log.i("FindMyPhone", "Notification command accepted")

        try {
            val intent = Intent(
                this,
                AlarmService::class.java
            )

            startForegroundService(intent)

        } catch (e: Exception) {
            Log.e(
                "FindMyPhone",
                "Could not start alarm from notification",
                e
            )
        }
    }
}

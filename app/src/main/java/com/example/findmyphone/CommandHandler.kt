
package com.example.findmyphone

import android.util.Log

object CommandHandler {

    fun processCommand(message: String): Boolean {

        val command = message.trim()

        if (command == "password_test") {
            Log.i(
                "FindMyPhone",
                "Test SMS command received successfully!"
            )
            return true
        }

        Log.i("FindMyPhone", "Command not recognized")
        return false
    }
}

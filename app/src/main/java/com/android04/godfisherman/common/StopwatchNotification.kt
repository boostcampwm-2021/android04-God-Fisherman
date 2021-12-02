package com.android04.godfisherman.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class StopwatchNotification {
    companion object {
        const val CHANNEL_ID = "Stopwatch_channel_id"

        fun createChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val serviceChannel = NotificationChannel(
                    CHANNEL_ID,
                    "Stopwatch Channel",
                    NotificationManager.IMPORTANCE_LOW
                )
                val manager = context.getSystemService(NotificationManager::class.java)
                manager?.createNotificationChannel(serviceChannel)
            }
        }
    }
}

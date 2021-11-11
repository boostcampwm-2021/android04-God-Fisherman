package com.android04.godfisherman.utils

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android04.godfisherman.R
import com.android04.godfisherman.ui.stopwatch.StopwatchActivity
import java.util.*

class StopwatchService:
    Service() {

    companion object {
        const val TIME_EXTRA = "timeExtra"
        const val TIME_SAVED = "timeSaved"
        const val SERVICE_DESTROYED = "serviceDestroyed"
        const val STOPWATCH_ENTER = "timeEnter"
        const val NOTIFICATION_ID = 10
    }
    private var saveTime = 0.0
    private val stopwatch = Timer()
    private lateinit var notification: NotificationCompat.Builder

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(passedIntent: Intent, flags: Int, startId: Int): Int {
        StopwatchActivity.isStopwatchServiceRunning = true

        val intent = Intent(this, StopwatchActivity::class.java)
        intent.action = STOPWATCH_ENTER
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(TIME_SAVED, saveTime)
        val pendingIntent = PendingIntent
            .getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        notification = NotificationCompat.Builder(this, StopwatchNotification.CHANNEL_ID)
            .setContentTitle("그물잠")
            .setSmallIcon(R.drawable.ic_fish_type)
            .setOngoing(true)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
        saveTime = passedIntent.getDoubleExtra(TIME_EXTRA, 0.0)
        startForeground(NOTIFICATION_ID, createNotification())
        stopwatch.scheduleAtFixedRate(StopwatchTask(), 0, 100)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("checkLifeCycle", "$this : onDestroy")
        stopwatch.cancel()
        val intent = Intent(SERVICE_DESTROYED)
        intent.putExtra(SERVICE_DESTROYED, saveTime)
        sendBroadcast(intent)
        NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID)
    }

    private inner class StopwatchTask() : TimerTask() {
        override fun run() {
            saveTime += 10
            updateNotification(saveTime)
        }
    }

    private fun createNotification(): Notification = notification.setContentText("00:00:00").build()

    private fun updateNotification(time: Double) {
        val updatedNotifiaction = notification.setContentText(time.toTimeSecond()).build()
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, updatedNotifiaction)
    }
}
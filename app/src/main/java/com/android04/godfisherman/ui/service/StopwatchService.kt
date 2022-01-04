package com.android04.godfisherman.ui.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android04.godfisherman.R
import com.android04.godfisherman.common.StopwatchNotification
import com.android04.godfisherman.ui.main.MainActivity
import com.android04.godfisherman.utils.toTimeSecond
import java.util.*

class StopwatchService : Service() {

    private var startTime = 0.0
    private val stopwatch = Timer()
    private lateinit var notification: NotificationCompat.Builder

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(passedIntent: Intent, flags: Int, startId: Int): Int {
        MainActivity.isStopwatchServiceRunning = true
        val intent = Intent(this, MainActivity::class.java)
        intent.action = STOPWATCH_ENTER
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(FROM_SERVICE, true)

        val pendingIntent = PendingIntent
            .getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        notification = NotificationCompat.Builder(this, StopwatchNotification.CHANNEL_ID)
            .setContentTitle("그물잠")
            .setSmallIcon(R.drawable.ic_fish_type)
            .setOngoing(true)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
        startTime = passedIntent.getDoubleExtra(TIME_EXTRA, 0.0)
        startForeground(NOTIFICATION_ID, createNotification())
        stopwatch.scheduleAtFixedRate(StopwatchTask(), 0, PERIOD)
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        stopwatch.cancel()
        val intent = Intent(SERVICE_DESTROYED)
        intent.putExtra(SERVICE_DESTROYED, startTime)
        sendBroadcast(intent)
        NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID)
    }

    private inner class StopwatchTask : TimerTask() {
        override fun run() {
            updateNotification(displayTime())
        }
    }

    private fun displayTime() = (System.currentTimeMillis().toDouble()-startTime) / 10

    private fun createNotification(): Notification = notification.setContentText("00:00:00").build()

    private fun updateNotification(time: Double) {
        val updatedNotification = notification.setContentText(time.toTimeSecond()).build()
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, updatedNotification)
    }

    companion object {
        const val TIME_EXTRA = "timeExtra"
        const val FROM_SERVICE = "fromService"
        const val SERVICE_DESTROYED = "serviceDestroyed"
        const val STOPWATCH_ENTER = "timeEnter"
        const val NOTIFICATION_ID = 10
        const val PERIOD = 1000L
        const val INTERVAL = 100.0
    }
}

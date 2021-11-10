package com.android04.godfisherman.ui.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.ActivityStopwatchBinding
import com.android04.godfisherman.ui.base.BaseActivity
import com.android04.godfisherman.utils.StopwatchService
import java.util.*
import android.app.ActivityManager
import androidx.core.app.NotificationManagerCompat


class StopwatchActivity :
    BaseActivity<ActivityStopwatchBinding, StopwatchViewModel>(R.layout.activity_stopwatch) {
    companion object {
        var isStopwatchServiceRunning = false
    }

    override val viewModel: StopwatchViewModel by viewModels()
    private lateinit var serviceIntent: Intent

    private val receiveTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, receiveIntent: Intent) {
            val receivedTime = receiveIntent.getDoubleExtra(StopwatchService.SERVICE_DESTROYED, 0.0)
            Log.d("receiveFromService", "리시브 실행 : $receivedTime")
            viewModel.passedTimeFromService(receivedTime)
            NotificationManagerCompat.from(context).cancel(StopwatchService.NOTIFICATION_ID)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        serviceIntent = Intent(applicationContext, StopwatchService::class.java)
        registerReceiver(receiveTime, IntentFilter(StopwatchService.SERVICE_DESTROYED))
    }

    override fun onResume() {
        super.onResume()
        Log.d("serviceRunning", "$isStopwatchServiceRunning")
        if (isStopwatchServiceRunning) {
            Log.d("serviceRunning", "서비스 종료 실행")
            stopService(serviceIntent)
            isStopwatchServiceRunning = false
        }
    }

    private fun passStopwatchToService(time: Double) {
        if (viewModel.isStopwatchStarted.value == true) {
            serviceIntent.putExtra(StopwatchService.TIME_EXTRA, time)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent)
            } else {
                startService(serviceIntent)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        passStopwatchToService(viewModel.time)
    }

}
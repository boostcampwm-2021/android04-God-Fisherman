package com.android04.godfisherman.utils

import android.util.Log
import java.util.*

class StopwatchManager(private val callback: (Double) -> (Unit), private var startTime: Double = System.currentTimeMillis().toDouble()) {
    private var resumeTime = 0.0
    private var saveTime = 0.0
    private lateinit var stopwatch: Timer

    fun end() {
        resumeTime = startTime
        stopwatch.cancel()
        callback(0.0)
    }

    fun start(period: Long, time: Double = 0.0) {
        startTime = time
        stopwatch = Timer()
        stopwatch.scheduleAtFixedRate(StopwatchTask(), 0, period)
    }

    fun resumeStopwatch(period: Long) {
        start(period, resumeTime)
    }

    fun getStartTime(): Double {
        return startTime
    }

    fun getSaveTime(): Double {
        return saveTime
    }

    private inner class StopwatchTask() : TimerTask() {
        override fun run() {
            Log.d("stopwatch", startTime.toString())
            Log.d("stopwatch", System.currentTimeMillis().toDouble().toString())
            callback(displayTime())
        }
    }

    private fun displayTime(): Double {
        saveTime = (System.currentTimeMillis().toDouble()-startTime) / 10
        return saveTime
    }
}

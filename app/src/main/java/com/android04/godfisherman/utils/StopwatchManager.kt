package com.android04.godfisherman.utils

import java.util.*

class StopwatchManager(private val callback:(Double) -> (Unit), private var time: Double = 0.0) {
    var resumeTime = 0.0
    private lateinit var stopwatch: Timer

    fun end(){
        resumeTime = time
        stopwatch.cancel()
        time = 0.0
        callback(time)
    }

    fun start(period: Long, startTime: Double = 0.0){
        time = startTime
        stopwatch = Timer()
        stopwatch.scheduleAtFixedRate(StopwatchTask(period), 0, period)
    }

    fun resumeStopwatch(period: Long){
        start(period, resumeTime)
    }

    fun getTime(): Double {
        return time
    }

    private inner class StopwatchTask(private val period: Long) : TimerTask() {
        override fun run() {
            time += period/10
            callback(time)
        }
    }
}
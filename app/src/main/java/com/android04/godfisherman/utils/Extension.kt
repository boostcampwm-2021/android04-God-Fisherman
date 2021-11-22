package com.android04.godfisherman.utils

import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

fun ByteBuffer.toByteArray(): ByteArray {
    rewind()
    val data = ByteArray(remaining())
    get(data)
    return data
}

fun Double.toTimeMilliSecond(): String{
    val time = this.roundToInt()
    val hour = time / 3600000
    val min = time % 3600000 / 60000
    val sec = time % 3600000 % 60000 / 1000
    val milli = time % 3600000 % 60000 % 1000
    return  String.format("%02d:%02d:%02d:%02d", hour, min, sec, milli)
}

fun Double.toTimeSecond(): String{
    val time = this.roundToInt()
    val hour = time / 3600000
    val min = time % 3600000 / 60000
    val sec = time % 3600000 % 60000 / 1000
    return  String.format("%02d:%02d:%02d", hour, min, sec)
}

fun Double.toTimeHourMinute(): String{
    val time = this.roundToInt()
    val hour = time / 3600000
    val min = time % 3600000 / 60000
    return  String.format("%02d시간 %02d분", hour, min)
}

fun Date.toDateString(): String{
    return SimpleDateFormat("MM월 dd일").format(this)
}
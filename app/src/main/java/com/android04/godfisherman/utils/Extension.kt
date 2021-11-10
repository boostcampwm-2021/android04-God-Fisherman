package com.android04.godfisherman.utils

import java.nio.ByteBuffer
import kotlin.math.roundToInt

fun ByteBuffer.toByteArray(): ByteArray {
    rewind()
    val data = ByteArray(remaining())
    get(data)
    return data
}

fun Double.toTimeMilliSecond(): String{
    val time = this.roundToInt() % 8640000
    val hour = time / 360000
    val min = time % 3600000 / 6000
    val sec = time % 3600000 % 6000 / 100
    val milli = time % 3600000 % 6000 % 100
    return  String.format("%02d:%02d:%02d:%02d", hour, min, sec, milli)
}

fun Double.toTimeSecond(): String{
    val time = this.roundToInt() % 8640000
    val hour = time / 360000
    val min = time % 3600000 / 6000
    val sec = time % 3600000 % 6000 / 100
    return  String.format("%02d:%02d:%02d", hour, min, sec)
}
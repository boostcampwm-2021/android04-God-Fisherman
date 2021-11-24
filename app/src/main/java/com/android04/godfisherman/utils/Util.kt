package com.android04.godfisherman.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

fun findHeightCenter(rect: List<Int>): Int {
    return (rect[0] + rect[1]) / 2
}

fun convertCentiMeter(length: Double): String {
    return roundBodySize(length).toString() + "cm"
}

fun roundBodySize(length: Double): Double {
    return String.format("%.1f", length).toDouble()
}

fun widthConvert(x: Int, imageWidth: Int, screenWidth: Int) : Int {
    val ratio = screenWidth.toFloat() / imageWidth

    return (x * ratio).toInt()
}

fun heightConvert(y: Int, imageHeight: Int, screenHeight: Int) : Int {
    val height = screenHeight.toFloat()
    val ratio = height / imageHeight

    return (y * ratio).toInt()
}

fun timeConvertUTC(time: Float): String {
    val format = SimpleDateFormat("HH:mm", Locale("ko", "KR"))
    return format.format(time * 1000)
}

fun timeConvertUTCFull(time: Float): String {
    val format = SimpleDateFormat("yyyy년 MM월 dd일 E요일 HH:mm시 기준", Locale("ko", "KR"))
    return format.format(time * 1000)
}

fun roundTime(time: String): String {
    val split = time.split(":")
    var hour = split.first().toInt()
    val min = split.last().toInt()

    if (min > 30) {
        hour += 1 % 24
    }

    return "${hour}시"
}

fun isLevelCorrect(x: Float, y: Float) = abs(x.roundToInt()) <= 1 && abs(y.roundToInt()) <= 1
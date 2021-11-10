package com.android04.godfisherman.utils

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
package com.android04.godfisherman.utils

fun findHeightCenter(rect: List<Int>): Int {
    return (rect[0] + rect[1]) / 2
}

fun convertCentiMeter(length: Float): String {
    return String.format("%.1f cm", length)
}
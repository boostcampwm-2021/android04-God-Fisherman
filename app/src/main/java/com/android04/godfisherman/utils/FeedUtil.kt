package com.android04.godfisherman.utils

import java.text.SimpleDateFormat
import java.util.*

fun getFeedDateFormat(date: Date): String {
    val dateFormat = SimpleDateFormat("MM월 dd일")
    dateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")

    return dateFormat.format(date)
}

fun getFeedTimeFormat(date: Date): String {
    val timeFormat = SimpleDateFormat("HH:mm")
    timeFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")

    return timeFormat.format(date)
}
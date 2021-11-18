package com.android04.godfisherman.ui.home

data class HomeCurrentWeather(
    val time: String,
    val sunrise: String,
    val sunset: String,
    val temp: String,
    val desc: String,
    val iconUrl: String
)

data class HomeDetailWeather(
    val time: String,
    val temp: String,
    val iconUrl: String
)
package com.android04.godfisherman.network.response

data class WeatherResponse(
    val current: CurrentWeatherData,
    val hourly: List<HourlyWeatherData>
)

data class CurrentWeatherData(
    val dt: Float,
    val sunrise: Float,
    val sunset: Float,
    val temp: Double,
    val weather: List<WeatherData>
)

data class HourlyWeatherData(
    val dt: Float,
    val temp: Double,
    val weather: List<WeatherData>
)

data class WeatherData(
    val main: String,
    val description: String,
    val icon: String
)

data class CurrentWeatherResponse(
    val weather: WeatherData,
    val main: CurrentMain,
    val sys: CurrentSys,
    val dt: Float
)

data class CurrentMain(
    val temp: Double
)

data class CurrentSys(
    val sunrise: Float,
    val sunset: Float,
)
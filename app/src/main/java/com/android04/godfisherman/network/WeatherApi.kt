package com.android04.godfisherman.network

import com.android04.godfisherman.network.WeatherApi.Companion.API_KEY
import com.android04.godfisherman.network.response.CurrentWeatherResponse
import com.android04.godfisherman.network.response.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

class WeatherApi {
    companion object {
        const val BASE_URL = "https://api.openweathermap.org"
        const val API_KEY = "abde9b59ff5383befdc8eddbe43642b0"
    }
}

interface WeatherApiService {

    @GET("/data/2.5/onecall")
    fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "kr"
    ): Call<WeatherResponse>

    @GET("/data/2.5/weather")
    fun getCurrentWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = API_KEY
    ): Call<CurrentWeatherResponse>
}
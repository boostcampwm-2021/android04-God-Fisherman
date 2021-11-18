package com.android04.godfisherman.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val gsonBuilder = GsonBuilder().setLenient().create()

    private val addressRetrofit: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(NaverApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
    }

    val naverApiService: NaverApiService by lazy {
        addressRetrofit.build().create(NaverApiService::class.java)
    }

    private val youtubeRetrofit: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(YoutubeApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
    }

    val youtubeApiService: YoutubeApiService by lazy {
        youtubeRetrofit.build().create(YoutubeApiService::class.java)
    }

    private val weatherRetrofit: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(WeatherApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
    }

    val weatherApiService: WeatherApiService by lazy {
        weatherRetrofit.build().create(WeatherApiService::class.java)
    }
}
package com.android04.godfisherman.data.datasource.homedatasource.remote

import com.android04.godfisherman.data.datasource.homedatasource.HomeDataSource
import com.android04.godfisherman.network.RetrofitClient
import com.android04.godfisherman.network.response.WeatherResponse
import com.android04.godfisherman.network.response.YoutubeResponse
import com.android04.godfisherman.utils.RepoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class HomeRemoteDataSourceImpl @Inject constructor(): HomeDataSource.RemoteDataSource {
    override suspend fun fetchYoutubeData(callback: RepoResponse<YoutubeResponse?>) {
        val call = RetrofitClient.youtubeApiService.getYoutubeData(
            q = fishingKey.random()
        )

        call.enqueue(object : Callback<YoutubeResponse>{
            override fun onResponse(
                call: Call<YoutubeResponse>,
                response: Response<YoutubeResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()

                    callback.invoke(true, body)
                } else {
                    onFailure(call, Throwable())
                }
            }

            override fun onFailure(call: Call<YoutubeResponse>, t: Throwable) {
                callback.invoke(false, null)
            }
        })
    }

    override suspend fun fetchWeatherData(lat: Double, lon: Double, callback: RepoResponse<WeatherResponse?>) {
        val call = RetrofitClient.weatherApiService.getWeatherData(lat, lon)

        call.enqueue(object : Callback<WeatherResponse>{
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    val current = response.body()?.current
                    val hourly = response.body()?.hourly

                    if (current != null && hourly != null) {
                        callback.invoke(true, response.body()!!)
                    } else {
                        onFailure(call, Throwable())
                    }
                } else {
                    onFailure(call, Throwable())
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                callback.invoke(false, null)
            }
        })
    }

    companion object {
        val fishingKey = listOf("민물낚시", "선상낚시", "바다낚시", "매운탕", "회뜨기", "낚시꿀팁", "낚시용품", "낚시대", "낚시터", "낚시명당")
    }
}
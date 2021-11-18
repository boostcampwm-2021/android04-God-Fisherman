package com.android04.godfisherman.network

import com.android04.godfisherman.network.YoutubeApi.Companion.API_KEY
import com.android04.godfisherman.network.response.YoutubeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

class YoutubeApi {
    companion object {
        const val BASE_URL = "https://www.googleapis.com"
        const val API_KEY = "AIzaSyC8YX2pn0p9IfU2KrLef48bUnoXzmSjm2Q"
    }
}

interface YoutubeApiService {

    @GET("/youtube/v3/search")
    fun getYoutubeData(
        @Query("part") part: String = "id, snippet",
        @Query("key") apiKey: String = API_KEY,
        @Query("q") q: String,
        @Query("maxResults") max: Int = 20,
        @Query("type") type: String = "video",
        @Query("regionCode") regionCode: String = "KR"
    ): Call<YoutubeResponse>
}
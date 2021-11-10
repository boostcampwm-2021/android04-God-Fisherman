package com.android04.godfisherman.network

import com.android04.godfisherman.network.response.RegionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

class NaverApi {
    companion object {
        const val BASE_URL = "https://naveropenapi.apigw.ntruss.com/"
        const val API_KEY_ID = "ctwbc3gk83"
        const val API_KEY_SECRET = "EdbBVl04WMmRV6eaxLpmAE2TVzUS9diLIfpUiw3V"
    }
}

interface NaverApiService {

    @GET("/map-reversegeocode/v2/gc")
    @Headers(
        "X-NCP-APIGW-API-KEY-ID: " + NaverApi.API_KEY_ID,
        "X-NCP-APIGW-API-KEY: " + NaverApi.API_KEY_SECRET
    )
    fun getAddress(
        @Query("coords") coords: String,
        @Query("output") output: String
    ): Call<RegionResponse>
}
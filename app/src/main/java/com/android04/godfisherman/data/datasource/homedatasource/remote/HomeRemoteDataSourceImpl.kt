package com.android04.godfisherman.data.datasource.homedatasource.remote

import com.android04.godfisherman.data.datasource.homedatasource.HomeDataSource
import com.android04.godfisherman.network.RetrofitClient
import com.android04.godfisherman.network.response.YoutubeResponse
import com.android04.godfisherman.utils.RepoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class HomeRemoteDataSourceImpl @Inject constructor(): HomeDataSource.RemoteDataSource {
    override suspend fun fetchYoutubeData(callback: RepoResponse<YoutubeResponse?>) {
        val call = RetrofitClient.youtubeApiService.getYoutubeData(
            q = "낚시"
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
}
package com.android04.godfisherman.data.datasource.locationdatasource.remote

import com.android04.godfisherman.data.datasource.locationdatasource.LocationRemoteDataSource
import com.android04.godfisherman.network.RetrofitClient
import com.android04.godfisherman.network.response.RegionResponse
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume

class LocationRemoteDataSourceImpl @Inject constructor() : LocationRemoteDataSource {

    override suspend fun fetchAddress(latitude: Double, longitude: Double): String? =
        suspendCancellableCoroutine { continuation ->
            val call = RetrofitClient.naverApiService.getAddress(
                "$longitude,$latitude",
                "json"
            )

            call.enqueue(object : Callback<RegionResponse> {
                override fun onResponse(
                    call: Call<RegionResponse>,
                    response: Response<RegionResponse>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()

                        if (body != null && body.results.isNotEmpty()) {
                            val region = body.results[0].region
                            continuation.resume("${region.area1.name} ${region.area2.name} ${region.area3.name}")
                        } else {
                            continuation.resume(null)
                        }
                    } else {
                        continuation.resume(null)
                    }
                }

                override fun onFailure(call: Call<RegionResponse>, t: Throwable) {
                    continuation.resume(null)
                }
            })
        }
}

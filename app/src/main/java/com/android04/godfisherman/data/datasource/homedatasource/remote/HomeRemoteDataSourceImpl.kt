package com.android04.godfisherman.data.datasource.homedatasource.remote

import com.android04.godfisherman.common.RepoResponse
import com.android04.godfisherman.common.constant.FishRankingRequest
import com.android04.godfisherman.data.datasource.homedatasource.HomeDataSource
import com.android04.godfisherman.data.datasource.uploaddatasource.remote.UploadRemoteDataSourceImpl.Companion.FEED_COLLECTION_NAME
import com.android04.godfisherman.data.datasource.uploaddatasource.remote.UploadRemoteDataSourceImpl.Companion.FISHING_RECORD_COLLECTION_NAME
import com.android04.godfisherman.data.dto.FishingRecord
import com.android04.godfisherman.data.dto.TypeInfo
import com.android04.godfisherman.network.RetrofitClient
import com.android04.godfisherman.network.response.WeatherResponse
import com.android04.godfisherman.network.response.YoutubeResponse
import com.android04.godfisherman.presentation.rankingdetail.RankingData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class HomeRemoteDataSourceImpl @Inject constructor() : HomeDataSource.RemoteDataSource {

    private val database = Firebase.firestore

    override suspend fun fetchYoutubeData(callback: RepoResponse<YoutubeResponse?>) {
        val call = RetrofitClient.youtubeApiService.getYoutubeData(
            q = fishingKey.random()
        )

        call.enqueue(object : Callback<YoutubeResponse> {
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

    override suspend fun fetchRankingList(request: FishRankingRequest): List<RankingData.HomeRankingData>? {
        val rankList: MutableList<RankingData.HomeRankingData> = mutableListOf()

        try {
            val querySnapshot = database.collectionGroup(FISHING_RECORD_COLLECTION_NAME)
                .orderBy(FISH_LENGTH_FIELD_NAME, Query.Direction.DESCENDING)
                .limit(request.count)
                .get()
                .await()

            val feedDocs = querySnapshot.documents

            feedDocs.forEach { doc ->
                val parentDoc = doc.reference.parent.parent!!.get().await()
                val typeInfo = parentDoc.toObject<TypeInfo>()
                val fishingRecord = doc.toObject<FishingRecord>()

                if (typeInfo != null && fishingRecord != null) {
                    rankList.add(
                        RankingData.HomeRankingData(
                            typeInfo.userName,
                            fishingRecord.fishType,
                            fishingRecord.fishLength
                        )
                    )
                }
            }
        } catch (_: Exception) {
            return null
        }

        return rankList
    }

    override suspend fun fetchWaitingRankingList(): List<RankingData.HomeWaitingRankingData> {
        var feedDocs: List<DocumentSnapshot>? = null
        val rankingMap: HashMap<String, Int> = HashMap()
        database.collection(FEED_COLLECTION_NAME)
            .whereNotEqualTo(FISHING_TIME_FIELD_NAME, 0)
            .get()
            .addOnSuccessListener {
                feedDocs = it.documents
            }.await()

        feedDocs?.forEach { doc ->
            val typeInfo = doc.toObject<TypeInfo>()
            typeInfo?.let {
                if (rankingMap.containsKey(it.userName)) {
                    rankingMap.put(it.userName, rankingMap.get(it.userName)!! + it.fishingTime)
                } else {
                    rankingMap.put(it.userName, it.fishingTime)
                }
            }
        }
        return rankingMap.map { RankingData.HomeWaitingRankingData(it.key, it.value) }
            .sortedByDescending { it.totalTime }
    }

    override suspend fun fetchWeatherData(
        lat: Double,
        lon: Double,
        callback: RepoResponse<WeatherResponse?>
    ) {
        val call = RetrofitClient.weatherApiService.getWeatherData(lat, lon)

        call.enqueue(object : Callback<WeatherResponse> {
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
        val fishingKey =
            listOf("민물낚시", "선상낚시", "바다낚시", "매운탕", "회뜨기", "낚시꿀팁", "낚시용품", "낚시대", "낚시터", "낚시명당")

        const val FISH_LENGTH_FIELD_NAME = "fishLength"
        const val FISHING_TIME_FIELD_NAME = "fishingTime"
    }
}

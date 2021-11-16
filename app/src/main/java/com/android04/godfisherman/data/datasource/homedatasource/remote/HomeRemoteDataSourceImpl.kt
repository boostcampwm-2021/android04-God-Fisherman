package com.android04.godfisherman.data.datasource.homedatasource.remote

import com.android04.godfisherman.data.datasource.homedatasource.HomeDataSource
import com.android04.godfisherman.data.entity.FishingRecord
import com.android04.godfisherman.data.entity.TypeInfo
import com.android04.godfisherman.network.RetrofitClient
import com.android04.godfisherman.network.response.YoutubeResponse
import com.android04.godfisherman.ui.home.RankingData
import com.android04.godfisherman.utils.RepoResponse
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

class HomeRemoteDataSourceImpl @Inject constructor(): HomeDataSource.RemoteDataSource {

    private val database = Firebase.firestore

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

    override suspend fun fetchRankingList(num: Long): List<RankingData.HomeRankingData>{
        var feedDocs: List<DocumentSnapshot>? = null
        val rankList: MutableList<RankingData.HomeRankingData> = mutableListOf()

        database.collectionGroup("fishingRecord")
            .orderBy("fishLength", Query.Direction.DESCENDING)
            .limit(num)
            .get()
            .addOnSuccessListener {
                feedDocs = it.documents
            }.await()

        feedDocs?.forEach { doc ->
            doc.reference.parent.parent!!.get().addOnSuccessListener { parentDoc ->
                val typeInfo = parentDoc.toObject<TypeInfo>()
                val fishingRecord = doc.toObject<FishingRecord>()
                if (typeInfo == null || fishingRecord == null) return@addOnSuccessListener
                rankList.add(RankingData.HomeRankingData(typeInfo.userName, fishingRecord.fishType, fishingRecord.fishLength))
            }.await()
        }
        return rankList
    }

    override suspend fun fetchWaitingRankingList(): List<RankingData.HomeWaitingRankingData> {
        var feedDocs: List<DocumentSnapshot>? = null
        val rankingMap: HashMap<String, Int> = HashMap()
        database.collection("Feed")
            .whereNotEqualTo("fishingTime", 0)
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
        return rankingMap.map { RankingData.HomeWaitingRankingData(it.key, it.value) }.sortedByDescending { it.totalTime }
    }
}

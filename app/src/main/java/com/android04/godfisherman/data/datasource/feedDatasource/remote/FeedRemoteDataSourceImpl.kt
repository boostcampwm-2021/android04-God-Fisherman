package com.android04.godfisherman.data.datasource.feedDatasource.remote

import com.android04.godfisherman.data.DTO.FeedDTO
import com.android04.godfisherman.data.datasource.feedDatasource.FeedDataSource
import com.android04.godfisherman.data.entity.FishingRecord
import com.android04.godfisherman.data.entity.Type
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FeedRemoteDataSourceImpl @Inject constructor() : FeedDataSource.RemoteDataSource {
    private val database = Firebase.firestore

    override suspend fun fetchFeedDataList(): List<FeedDTO> {
        val result = mutableListOf<FeedDTO>()

        val feedRef = database.collection("Feed")

        var feedDocs: List<DocumentSnapshot>? = null
        feedRef.get().addOnSuccessListener {
            feedDocs = it.documents
        }.await()

        feedDocs?.let { feedList ->
            feedList.forEach { feed ->
                val feedType: Type? = feed.toObject<Type>()

                feed.reference.collection("fishingRecord").get()
                    .addOnSuccessListener { docs ->
                        val fishingRecordList = mutableListOf<FishingRecord>()

                        docs.documents.forEach { recordDoc ->
                            val fishingRecord = recordDoc.toObject<FishingRecord>()

                            if (fishingRecord != null) {
                                fishingRecordList.add(fishingRecord)
                            }
                        }

                        if (feedType != null && fishingRecordList.isNotEmpty()) {
                            result.add(FeedDTO(feedType, fishingRecordList.toList()))
                        }

                    }.addOnFailureListener {
                        //Todo
                    }
                    .await()
            }
        }

        return result
    }
}

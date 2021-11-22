package com.android04.godfisherman.data.datasource.feedDatasource.remote

import com.android04.godfisherman.common.Type
import com.android04.godfisherman.data.DTO.FeedDTO
import com.android04.godfisherman.data.datasource.feedDatasource.FeedDataSource
import com.android04.godfisherman.data.entity.FishingRecord
import com.android04.godfisherman.data.entity.TypeInfo
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FeedRemoteDataSourceImpl @Inject constructor() : FeedDataSource.RemoteDataSource {
    private val database = Firebase.firestore

    override suspend fun fetchSnapshotList(type: Type, startTimeStamp: Timestamp): List<DocumentSnapshot>? {
        var feedRef = when (type) {
            Type.ALL -> database.collection("Feed")
            Type.PHOTO -> database.collection("Feed").whereEqualTo("isTimeline", false)
            Type.TIMELINE -> database.collection("Feed").whereEqualTo("isTimeline", true)
        }

        feedRef = feedRef.orderBy("id", Query.Direction.DESCENDING).whereLessThan("id",
            startTimeStamp
        ).limit(5)
        var feedDocs: List<DocumentSnapshot>? = null
        feedRef.get().addOnSuccessListener {
            feedDocs = it.documents
        }.await()
        return feedDocs
    }

    override suspend fun fetchFeedDataList(feedDocs: List<DocumentSnapshot>?): List<FeedDTO> {
        val result = mutableListOf<FeedDTO>()
        feedDocs?.let { feedList ->
            feedList.forEach { feed ->
                val feedTypeInfo: TypeInfo? = feed.toObject<TypeInfo>()

                feed.reference.collection("fishingRecord").get()
                    .addOnSuccessListener { docs ->
                        val fishingRecordList = mutableListOf<FishingRecord>()

                        docs.documents.forEach { recordDoc ->
                            val fishingRecord = recordDoc.toObject<FishingRecord>()

                            if (fishingRecord != null) {
                                fishingRecordList.add(fishingRecord)
                            }
                        }

                        if (feedTypeInfo != null && fishingRecordList.isNotEmpty()) {
                            result.add(FeedDTO(feedTypeInfo, fishingRecordList.toList()))
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

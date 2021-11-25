package com.android04.godfisherman.data.datasource.feedDatasource.remote

import com.android04.godfisherman.common.Type
import com.android04.godfisherman.data.DTO.FeedDTO
import com.android04.godfisherman.data.datasource.feedDatasource.FeedDataSource
import com.android04.godfisherman.data.datasource.uploadDataSource.remote.UploadRemoteDataSourceImpl.Companion.FEED_COLLECTION_NAME
import com.android04.godfisherman.data.datasource.uploadDataSource.remote.UploadRemoteDataSourceImpl.Companion.FISHING_RECORD_COLLECTION_NAME
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

    override suspend fun fetchSnapshotList(
        type: Type,
        startTimeStamp: Timestamp
    ): List<DocumentSnapshot>? {
        var feedRef = database.collection(FEED_COLLECTION_NAME).run {
            when (type) {
                Type.PHOTO -> whereEqualTo(TYPE_FIELD_NAME, false)
                Type.TIMELINE -> whereEqualTo(TYPE_FIELD_NAME, true)
                Type.ALL -> this
            }
        }

        feedRef = feedRef.orderBy(FEED_IDENTIFIER_NAME, Query.Direction.DESCENDING)
            .whereLessThan(FEED_IDENTIFIER_NAME, startTimeStamp)
            .limit(5)

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

                feed.reference.collection(FISHING_RECORD_COLLECTION_NAME)
                    .orderBy(FISHING_RECORD_IDENTIFIER_NAME, Query.Direction.ASCENDING)
                    .get()
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

                    }.await()
            }
        }
        return result
    }

    companion object {
        const val TYPE_FIELD_NAME = "isTimeline"
        const val FEED_IDENTIFIER_NAME = "id"
        const val FISHING_RECORD_IDENTIFIER_NAME = "id"
    }
}

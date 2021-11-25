package com.android04.godfisherman.data.datasource.feeddatasource

import com.android04.godfisherman.common.constant.Type
import com.android04.godfisherman.data.dto.FeedDTO
import com.android04.godfisherman.data.localdatabase.entity.FishingRecordCached
import com.android04.godfisherman.data.localdatabase.entity.TypeInfoCached
import com.android04.godfisherman.data.localdatabase.entity.TypeInfoWithFishingRecords
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*

interface FeedDataSource {

    interface LocalDataSource {
        suspend fun saveFeed(
            typeInfoCached: TypeInfoCached,
            recordCachedList: List<FishingRecordCached>
        )

        suspend fun loadFeedDataList(type: Type, next: Date): List<TypeInfoWithFishingRecords>
        suspend fun deleteAll()
    }

    interface RemoteDataSource {
        suspend fun fetchFeedDataList(feedDocs: List<DocumentSnapshot>?): List<FeedDTO>
        suspend fun fetchSnapshotList(type: Type, startSnapshot: Timestamp): List<DocumentSnapshot>?
    }

}

package com.android04.godfisherman.data.datasource.feedDatasource

import com.android04.godfisherman.common.Type
import com.android04.godfisherman.data.DTO.FeedDTO
import com.android04.godfisherman.localdatabase.entity.FishingRecordCached
import com.android04.godfisherman.localdatabase.entity.TypeInfoCached

interface FeedDataSource {

    interface LocalDataSource {
        suspend fun saveFeed(typeInfoCached: TypeInfoCached, recordCachedList: List<FishingRecordCached>)
    }

    interface RemoteDataSource {
        suspend fun fetchFeedDataList(type: Type): List<FeedDTO>?
    }

}

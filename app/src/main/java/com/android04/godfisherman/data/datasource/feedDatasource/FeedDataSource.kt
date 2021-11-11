package com.android04.godfisherman.data.datasource.feedDatasource

import com.android04.godfisherman.data.entity.FishingRecord
import com.android04.godfisherman.data.entity.Type

interface FeedDataSource {

    interface LocalDataSource {
    }

    interface RemoteDataSource {
        suspend fun fetchFeedDataList(): List<Pair<Type, List<FishingRecord>>>?
    }

}

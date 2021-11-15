package com.android04.godfisherman.data.datasource.feedDatasource

import com.android04.godfisherman.common.Type
import com.android04.godfisherman.data.DTO.FeedDTO

interface FeedDataSource {

    interface LocalDataSource {
    }

    interface RemoteDataSource {
        suspend fun fetchFeedDataList(type: Type): List<FeedDTO>?
    }

}

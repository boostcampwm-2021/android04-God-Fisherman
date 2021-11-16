package com.android04.godfisherman.data.datasource.feedDatasource.local

import com.android04.godfisherman.data.datasource.feedDatasource.FeedDataSource
import com.android04.godfisherman.localdatabase.dao.FeedCachedDao
import com.android04.godfisherman.localdatabase.entity.FishingRecordCached
import com.android04.godfisherman.localdatabase.entity.TypeInfoCached
import javax.inject.Inject

class FeedLocalDataSourceImpl @Inject constructor(
    private val feedCachedDao: FeedCachedDao
) : FeedDataSource.LocalDataSource {

    override suspend fun saveFeed(
        typeInfoCached: TypeInfoCached,
        recordCachedList: List<FishingRecordCached>
    ) {
        feedCachedDao.insertFeed(typeInfoCached, recordCachedList)
    }

}
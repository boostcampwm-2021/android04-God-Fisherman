package com.android04.godfisherman.data.datasource.feeddatasource.local

import com.android04.godfisherman.common.constant.Type
import com.android04.godfisherman.data.datasource.feeddatasource.FeedDataSource
import com.android04.godfisherman.data.localdatabase.dao.FeedCachedDao
import com.android04.godfisherman.data.localdatabase.entity.FishingRecordCached
import com.android04.godfisherman.data.localdatabase.entity.TypeInfoCached
import com.android04.godfisherman.data.localdatabase.entity.TypeInfoWithFishingRecords
import java.util.*
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

    override suspend fun loadFeedDataList(type: Type, next: Date): List<TypeInfoWithFishingRecords> {
        return when (type) {
            Type.ALL -> feedCachedDao.getTypeInfosWithFishingRecords(next)
            Type.PHOTO -> feedCachedDao.getTypeInfosWithFishingRecordsFiltered(false, next)
            Type.TIMELINE -> feedCachedDao.getTypeInfosWithFishingRecordsFiltered(true, next)
        }
    }

    override suspend fun deleteAll() {
        feedCachedDao.deleteAll()
    }
}
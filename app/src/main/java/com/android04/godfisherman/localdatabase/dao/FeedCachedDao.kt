package com.android04.godfisherman.localdatabase.dao

import androidx.room.*
import com.android04.godfisherman.localdatabase.entity.FishingRecordCached
import com.android04.godfisherman.localdatabase.entity.TypeInfoCached
import com.android04.godfisherman.localdatabase.entity.TypeInfoWithFishingRecords

@Dao
interface FeedCachedDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeed(
        typeInfoCached: TypeInfoCached,
        recordCachedList: List<FishingRecordCached>
    ) {
        insertTypeInfoCached(typeInfoCached)
        insertRecordCachedList(recordCachedList)
    }

    @Insert
    suspend fun insertTypeInfoCached(typeInfoCached: TypeInfoCached)

    @Insert
    suspend fun insertRecordCachedList(recordCachedList: List<FishingRecordCached>)

    @Transaction
    @Query("SELECT * FROM TypeInfoCached ORDER BY id DESC")
    suspend fun getTypeInfosWithFishingRecords(): List<TypeInfoWithFishingRecords>
}
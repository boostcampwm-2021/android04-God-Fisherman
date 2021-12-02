package com.android04.godfisherman.data.localdatabase.dao

import androidx.room.*
import com.android04.godfisherman.data.localdatabase.entity.FishingRecordCached
import com.android04.godfisherman.data.localdatabase.entity.TypeInfoCached
import com.android04.godfisherman.data.localdatabase.entity.TypeInfoWithFishingRecords
import java.util.*

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTypeInfoCached(typeInfoCached: TypeInfoCached)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecordCachedList(recordCachedList: List<FishingRecordCached>)

    @Transaction
    @Query("DELETE FROM TypeInfoCached")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM TypeInfoCached WHERE id < :id ORDER BY id DESC LIMIT 5")
    suspend fun getTypeInfosWithFishingRecords(id: Date): List<TypeInfoWithFishingRecords>

    @Transaction
    @Query("SELECT * FROM TypeInfoCached WHERE isTimeline = :isTimeLine AND id < :id ORDER BY id DESC LIMIT 5")
    suspend fun getTypeInfosWithFishingRecordsFiltered(isTimeLine: Boolean, id: Date): List<TypeInfoWithFishingRecords>
}

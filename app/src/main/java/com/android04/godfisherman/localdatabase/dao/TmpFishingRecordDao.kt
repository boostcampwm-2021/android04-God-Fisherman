package com.android04.godfisherman.localdatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.android04.godfisherman.localdatabase.entity.TmpFishingRecord

@Dao
interface TmpFishingRecordDao {

    @Query("SELECT * FROM temporary_fishing_record ORDER BY date ASC")
    fun getTmpRecords(): List<TmpFishingRecord>

    @Insert
    suspend fun insert(record: TmpFishingRecord)

    @Query("DELETE FROM temporary_fishing_record")
    suspend fun deleteAll()
}

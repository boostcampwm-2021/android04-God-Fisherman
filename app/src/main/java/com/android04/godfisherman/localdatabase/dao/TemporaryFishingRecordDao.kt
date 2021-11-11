package com.android04.godfisherman.localdatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.android04.godfisherman.localdatabase.entity.TemporaryFishingRecord

@Dao
interface TemporaryFishingRecordDao {

    @Query("SELECT * FROM temporary_fishing_record ORDER BY date ASC")
    fun getTemporaryRecords(): List<TemporaryFishingRecord>

    @Insert
    suspend fun insert(record: TemporaryFishingRecord)

    @Query("DELETE FROM temporary_fishing_record")
    suspend fun deleteAll()
}

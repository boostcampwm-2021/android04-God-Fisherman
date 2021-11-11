package com.android04.godfisherman.localdatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.android04.godfisherman.localdatabase.entity.TemporaryFishingRecord

@Dao
interface TemporaryFishingRecordDao {

    @Query("SELECT * FROM temporary_fishing_record WHERE userId == :userId ORDER BY date ASC")
    fun getTemporaryRecords(userId: String): List<TemporaryFishingRecord>

    @Insert
    suspend fun insert(record: TemporaryFishingRecord)

    @Query("DELETE FROM temporary_fishing_record WHERE userId == :userId")
    suspend fun delete(userId: String)
}

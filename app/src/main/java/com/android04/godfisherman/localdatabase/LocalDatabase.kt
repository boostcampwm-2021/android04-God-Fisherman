package com.android04.godfisherman.localdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android04.godfisherman.localdatabase.dao.TmpFishingRecordDao
import com.android04.godfisherman.localdatabase.entity.TmpFishingRecord

@Database(entities = [TmpFishingRecord::class], version = 1, exportSchema = false)
@TypeConverters(DatabaseTypeConverter::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun temporaryFishingRecordDao(): TmpFishingRecordDao

}

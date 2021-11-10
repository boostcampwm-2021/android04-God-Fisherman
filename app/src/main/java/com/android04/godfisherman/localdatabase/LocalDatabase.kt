package com.android04.godfisherman.localdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android04.godfisherman.localdatabase.dao.TemporaryFishingRecordDao
import com.android04.godfisherman.localdatabase.entity.TemporaryFishingRecord

@Database(entities = [TemporaryFishingRecord::class], version = 1, exportSchema = false)
@TypeConverters(DatabaseTypeConverter::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun temporaryFishingRecordDao(): TemporaryFishingRecordDao

}

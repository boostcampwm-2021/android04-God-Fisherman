package com.android04.godfisherman.data.localdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android04.godfisherman.data.localdatabase.dao.FeedCachedDao
import com.android04.godfisherman.data.localdatabase.dao.TmpFishingRecordDao
import com.android04.godfisherman.data.localdatabase.entity.FishingRecordCached
import com.android04.godfisherman.data.localdatabase.entity.TmpFishingRecord
import com.android04.godfisherman.data.localdatabase.entity.TypeInfoCached
import com.android04.godfisherman.utils.DatabaseTypeConverter

@Database(
    entities = [TmpFishingRecord::class, TypeInfoCached::class, FishingRecordCached::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DatabaseTypeConverter::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun temporaryFishingRecordDao(): TmpFishingRecordDao

    abstract fun feedCachedDao(): FeedCachedDao
}

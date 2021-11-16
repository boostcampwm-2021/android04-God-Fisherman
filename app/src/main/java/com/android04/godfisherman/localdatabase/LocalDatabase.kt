package com.android04.godfisherman.localdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android04.godfisherman.localdatabase.dao.FeedCachedDao
import com.android04.godfisherman.localdatabase.dao.TmpFishingRecordDao
import com.android04.godfisherman.localdatabase.entity.FishingRecordCached
import com.android04.godfisherman.localdatabase.entity.TmpFishingRecord
import com.android04.godfisherman.localdatabase.entity.TypeInfoCached
import org.w3c.dom.TypeInfo

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

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

    // TODO Hilt로 의존성 받아올 수 있는지 확인하고 주석 삭제
//    companion object {
//
//        @Volatile
//        private var INSTANCE: LocalDatabase? = null
//
//        fun getInstance(context: Context): LocalDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    LocalDatabase::class.java,
//                    "local_database"
//                ).build()
//                INSTANCE = instance
//                // return instance
//                instance
//            }
//        }
//    }
}

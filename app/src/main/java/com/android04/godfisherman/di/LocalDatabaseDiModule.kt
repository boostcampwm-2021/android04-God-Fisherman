package com.android04.godfisherman.di

import android.content.Context
import androidx.room.Room
import com.android04.godfisherman.localdatabase.LocalDatabase
import com.android04.godfisherman.localdatabase.dao.TemporaryFishingRecordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDatabaseDiModule {

    @Provides
    fun providesTemporaryFishingRecordDao(localDatabase: LocalDatabase): TemporaryFishingRecordDao {
        return localDatabase.temporaryFishingRecordDao()
    }

    @Provides
    @Singleton
    fun providesLocalDatabase(@ApplicationContext context: Context) : LocalDatabase{
        return Room.databaseBuilder(
            context.applicationContext,
            LocalDatabase::class.java,
            "local_database"
        ).build()
    }
}

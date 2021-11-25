package com.android04.godfisherman.common.di

import android.content.Context
import androidx.room.Room
import com.android04.godfisherman.data.localdatabase.LocalDatabase
import com.android04.godfisherman.data.localdatabase.dao.FeedCachedDao
import com.android04.godfisherman.data.localdatabase.dao.TmpFishingRecordDao
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
    fun providesTemporaryFishingRecordDao(localDatabase: LocalDatabase): TmpFishingRecordDao {
        return localDatabase.temporaryFishingRecordDao()
    }

    @Provides
    fun providesFeedCachedDao(localDatabase: LocalDatabase): FeedCachedDao {
        return localDatabase.feedCachedDao()
    }

    @Provides
    @Singleton
    fun providesLocalDatabase(@ApplicationContext context: Context): LocalDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            LocalDatabase::class.java,
            "local_database"
        ).build()
    }
}

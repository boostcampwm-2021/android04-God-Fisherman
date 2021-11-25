package com.android04.godfisherman.di

import android.content.Context
import com.android04.godfisherman.common.NetworkChecker
import com.android04.godfisherman.data.cache.HomeInfoCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDiModule {

    @Singleton
    @Provides
    fun provideNetworkChecker(@ApplicationContext context: Context): NetworkChecker =
        NetworkChecker(context)

    @Singleton
    @Provides
    fun provideHomeInfoCache(): HomeInfoCache = HomeInfoCache()

    @Singleton
    @ApplicationScope
    @Provides
    fun providesCoroutineScope(
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CoroutineScope {
        return CoroutineScope(SupervisorJob() + ioDispatcher)
    }
}
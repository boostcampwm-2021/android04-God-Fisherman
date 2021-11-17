package com.android04.godfisherman.di

import android.content.Context
import com.android04.godfisherman.common.NetworkChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDiModule {

    @Singleton
    @Provides
    fun provideNetworkChecker(@ApplicationContext context: Context): NetworkChecker =
        NetworkChecker(context)
}
package com.android04.godfisherman.di

import com.android04.godfisherman.data.datasource.remote.LocationRemoteDataSource
import com.android04.godfisherman.data.datasource.remote.LocationRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class LocationDataSourceDiModule {

    @Binds
    abstract fun bindRemoteLocationDataSource(
        locationRemoteDataSourceImpl: LocationRemoteDataSourceImpl
    ): LocationRemoteDataSource
}

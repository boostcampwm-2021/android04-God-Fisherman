package com.android04.godfisherman.data.repository

import com.android04.godfisherman.data.datasource.remote.LocationRemoteDataSource
import com.android04.godfisherman.utils.SharedPreferenceManager
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val remoteDataSource: LocationRemoteDataSource,
    private val preferenceManager: SharedPreferenceManager
) {

    suspend fun updateLocation(latitude: Double, longitude: Double) {
        val currentAddress = remoteDataSource.fetchAddress(latitude, longitude)
        preferenceManager.saveString(SharedPreferenceManager.PREF_LOCATION, currentAddress.getAddressLine(0))
    }
}

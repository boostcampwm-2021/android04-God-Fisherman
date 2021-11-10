package com.android04.godfisherman.data.repository

import android.location.Location
import android.util.Log
import com.android04.godfisherman.data.datasource.remote.LocationRemoteDataSource
import com.android04.godfisherman.utils.SharedPreferenceManager
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val remoteDataSource: LocationRemoteDataSource,
    private val preferenceManager: SharedPreferenceManager
) {

    fun updateLocation(location: Location?): String {
        if (location != null) {
            val currentAddress =
                remoteDataSource.fetchAddress(location.latitude, location.longitude)
            Log.d("GPS", "location : $currentAddress")
            preferenceManager.saveString(SharedPreferenceManager.PREF_LOCATION, currentAddress)
        } else {
            Log.d("GPS", "location : 위치를 불러올 수 없습니다.")
            preferenceManager.saveString(SharedPreferenceManager.PREF_LOCATION, "위치를 불러올 수 없습니다")
        }
        return preferenceManager.getString(SharedPreferenceManager.PREF_LOCATION)
            ?: "위치를 불러올 수 없습니다"
    }
}

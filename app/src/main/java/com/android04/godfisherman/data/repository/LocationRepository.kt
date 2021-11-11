package com.android04.godfisherman.data.repository

import android.location.Location
import com.android04.godfisherman.data.datasource.remote.LocationRemoteDataSource
import com.android04.godfisherman.utils.SharedPreferenceManager
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val remoteDataSource: LocationRemoteDataSource,
    private val preferenceManager: SharedPreferenceManager
) {

    suspend fun updateLocation(location: Location?): String {
        if (location != null) {
            val currentAddress =
                remoteDataSource.fetchAddress(location.latitude, location.longitude)

            if (currentAddress != null) {
                preferenceManager.saveString(SharedPreferenceManager.PREF_LOCATION, currentAddress)
            } else {
                return "네트워크 연결이 좋지 않아 위치를 불러올 수 없습니다"
            }

        } else {
            return "GPS를 켜주세요"
        }

        return preferenceManager.getString(SharedPreferenceManager.PREF_LOCATION)
            ?: "위치를 불러올 수 없습니다"
    }
}

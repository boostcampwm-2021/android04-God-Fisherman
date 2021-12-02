package com.android04.godfisherman.data.repository

import android.location.Location
import com.android04.godfisherman.common.Result
import com.android04.godfisherman.common.SharedPreferenceManager
import com.android04.godfisherman.data.datasource.locationdatasource.LocationRemoteDataSource
import com.android04.godfisherman.presentation.main.Gps
import com.android04.godfisherman.utils.GPS_ERROR
import com.android04.godfisherman.utils.NETWORK_ERROR
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val remoteDataSource: LocationRemoteDataSource,
    private val preferenceManager: SharedPreferenceManager
) {

    suspend fun updateAddress(): Result<String> {
        val location = loadLocation()

        if (location != null) {
            val currentAddress =
                remoteDataSource.fetchAddress(location.latitude, location.longitude)

            if (currentAddress != null) {
                preferenceManager.saveString(SharedPreferenceManager.PREF_LOCATION, currentAddress)
            } else {
                return Result.Fail(NETWORK_ERROR)
                // 주소를 얻을 수 없는 좌표의 경우
            }
        } else {
            return Result.Fail(GPS_ERROR)
        }

        val address = preferenceManager.getString(SharedPreferenceManager.PREF_LOCATION)

        return if (address != null) {
            Result.Success(address)
        } else {
            Result.Fail(GPS_ERROR)
        }
    }

    fun saveLocation(location: Location?) {
        if (location != null) preferenceManager.saveGps(Gps(location.longitude, location.latitude))
    }

    fun loadLocation() = preferenceManager.loadGps()
}

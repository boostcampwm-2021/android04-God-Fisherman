package com.android04.godfisherman.data.repository

import android.location.Location
import android.util.Log
import com.android04.godfisherman.data.DTO.Gps
import com.android04.godfisherman.data.datasource.remote.LocationRemoteDataSource
import com.android04.godfisherman.utils.SharedPreferenceManager
import javax.inject.Inject
import com.android04.godfisherman.common.Result

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
                return Result.Fail("네트워크 연결이 좋지 않아 위치를 불러올 수 없습니다")
            }
        } else {
            return Result.Fail("GPS를 켜주세요")
        }

        val address = preferenceManager.getString(SharedPreferenceManager.PREF_LOCATION)

        return if (address != null) {
            Result.Success(address)
        } else {
            Result.Fail("위치를 불러올 수 없습니다 다시 실행해주세요")
        }
    }

    fun saveLocation(location: Location?) {
        if (location != null) preferenceManager.saveGps(Gps(location.longitude, location.latitude))
    }

    fun loadLocation() = preferenceManager.getGps()
}

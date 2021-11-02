package com.android04.godfisherman.data.repository

import com.android04.godfisherman.data.datasource.remote.LocationRemoteDataSource
import com.android04.godfisherman.utils.SharedPreferenceManager
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val remoteDataSource: LocationRemoteDataSource,
    private val preferenceManager: SharedPreferenceManager
) {

    fun updateLocation(latitude: Double, longitude: Double) {
        // TODO 위도, 경도 기반의 위치 정보를 remoteDataSource 로 부터 전달받아야한다.
        preferenceManager.saveString(SharedPreferenceManager.PREF_LOCATION, "$latitude,$longitude")
    }
}

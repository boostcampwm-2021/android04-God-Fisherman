package com.android04.godfisherman.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import com.android04.godfisherman.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationListener {
    var locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    lateinit var updateCallback: () -> (Unit)
    @SuppressLint("MissingPermission")
    fun setLocationUpdate(callback: () -> (Unit)) {
        updateCallback = callback
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 5f, this)

        } else {
            Log.d("LocationUpdate", "위치 센서 접근 불가")
            showToast(context, "위치 센서 접근 불가")
        }
    }

    @SuppressLint("MissingPermission")
    fun getLocation(): Location? {
        var bestLocation: Location? = null
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            bestLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        } else {
            Log.d("LocationUpdate", "위치 센서 접근 불가")
            showToast(context, "위치 센서 접근 불가")
        }
        Log.d("LocationUpdate", "getLocation() : $bestLocation")
        return bestLocation
    }

    override fun onLocationChanged(location: Location) {
        Log.d("LocationUpdate", "위치갱신 : $location")
        updateCallback()
        stopLocationUpdate()
    }

    private fun stopLocationUpdate(){
        Log.d("LocationChanged", "위치갱신 종료")
        locationManager.removeUpdates(this)
    }

}

package com.android04.godfisherman.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject

class LocationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationListener {
    var locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @SuppressLint("MissingPermission")
    fun setLocationUpdate() {
        var bestLocation: Location? = null
        if (isGrantedLocationPermission(context)) {
            val providers = locationManager.getProviders(true)
            for (provider in providers) {
                if (locationManager.isProviderEnabled(provider)) {
                    locationManager.requestLocationUpdates(provider, 1000, 5f, this)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getLocation(): Location? {
        var bestLocation: Location? = null
        if (isGrantedLocationPermission(context)) {
            val providers = locationManager.getProviders(true)
            for (provider in providers) {
                if (locationManager.isProviderEnabled(provider)) {
                    val currentLocation = locationManager.getLastKnownLocation(provider) ?: continue
                    if (bestLocation == null || currentLocation.accuracy < bestLocation.accuracy) {
                        bestLocation = currentLocation
                    }
                }
            }
        }
        locationManager.removeUpdates(this)
        return bestLocation
    }

    override fun onLocationChanged(p0: Location) {
        // TODO: 위치 변경 시 처리 필요 없음
    }

}

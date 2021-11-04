package com.android04.godfisherman.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocationHelper @Inject constructor(
    @ApplicationContext private val context: Context
){
    var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @SuppressLint("MissingPermission")
    fun getLocation(): Location? {
        var bestLocation: Location? = null

        if (isGrantedLocationPermission(context)) {
            val providers = locationManager.getProviders(true)
            for (provider in providers) {
                val currentLocation = locationManager.getLastKnownLocation(provider) ?: continue
                if (bestLocation == null || currentLocation.accuracy < bestLocation.accuracy) {
                    bestLocation = currentLocation
                }
            }
        }
        return bestLocation
    }
}

package com.android04.godfisherman.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
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
        if (isGrantedLocationPermission(context)) {
            val providers = locationManager.getProviders(true)
            for (provider in providers) {
                if (locationManager.isProviderEnabled(provider)) {
                    locationManager.requestLocationUpdates(provider, 100, 5f, this)
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
        return bestLocation
    }

    override fun onLocationChanged(p0: Location) {
        Log.d("LocationUpdate", "위치갱신")
        updateCallback()
        stopLocationUpdate()
    }

    private fun stopLocationUpdate(){
        Log.d("LocationChanged", "위치갱신 종료")
        locationManager.removeUpdates(this)
    }
    
}

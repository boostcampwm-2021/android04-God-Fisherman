package com.android04.godfisherman.common

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Toast
import com.android04.godfisherman.utils.GPS_ERROR
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
            Toast.makeText(context, GPS_ERROR, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    fun getLocation(): Location? {
        var bestLocation: Location? = null

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            bestLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        } else {
            Toast.makeText(context, GPS_ERROR, Toast.LENGTH_SHORT).show()
        }

        return bestLocation
    }

    override fun onLocationChanged(location: Location) {
        updateCallback()
        stopLocationUpdate()
    }

    private fun stopLocationUpdate() {
        locationManager.removeUpdates(this)
    }
}

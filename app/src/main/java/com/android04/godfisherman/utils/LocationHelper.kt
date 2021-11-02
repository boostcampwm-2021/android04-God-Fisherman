package com.android04.godfisherman.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocationHelper @Inject constructor(
    @ApplicationContext private val context: Context
){
    var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    fun getLocation(): Location? {
        var bestLocation: Location? = null

        if (
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO 사용자에게 권한 받아온 후 바로 위치 정보 가져오기
            // ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
        } else {

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

//    companion object {
//        const val REQUEST_CODE_LOCATION = 2
//    }
}

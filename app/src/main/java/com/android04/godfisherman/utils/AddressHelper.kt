package com.android04.godfisherman.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AddressHelper @Inject constructor(
    @ApplicationContext private val context: Context
){
    private val geocoder = Geocoder(context)

    fun getAddress(latitude: Double, longitude: Double): Address {
        return geocoder.getFromLocation(latitude, longitude, 1)[0]
    }
}

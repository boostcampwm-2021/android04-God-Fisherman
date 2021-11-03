package com.android04.godfisherman.data.datasource.remote

import android.location.Address
import com.android04.godfisherman.utils.AddressHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRemoteDataSourceImpl @Inject constructor(
    private val addressHelper: AddressHelper
)  : LocationRemoteDataSource {

    override suspend fun fetchAddress(latitude: Double, longitude: Double): Address = withContext(Dispatchers.IO) {
        addressHelper.getAddress(latitude, longitude)
    }
}

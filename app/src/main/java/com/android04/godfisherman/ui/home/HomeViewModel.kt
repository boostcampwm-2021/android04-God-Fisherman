package com.android04.godfisherman.ui.home

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android04.godfisherman.data.repository.LocationRepository
import com.android04.godfisherman.utils.LocationHelper
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val locationHelper: LocationHelper
) : ViewModel() {

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> = _address
    
    fun updateLocation() {
        locationHelper.setLocationUpdate()
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val location = locationHelper.getLocation()
                _address.postValue(locationRepository.updateLocation(location))
            }
        }
    }
}

package com.android04.godfisherman.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android04.godfisherman.data.repository.LocationRepository
import com.android04.godfisherman.utils.LocationHelper
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val locationHelper: LocationHelper
) : ViewModel() {
  
    fun updateLocation() {
        val location = locationHelper.getLocation() ?: return
        val (latitude, longitude) = location.latitude to location.longitude
        viewModelScope.launch {
            locationRepository.updateLocation(latitude, longitude)
        }
    }
    
}

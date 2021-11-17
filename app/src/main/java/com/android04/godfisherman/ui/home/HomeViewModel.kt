package com.android04.godfisherman.ui.home

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android04.godfisherman.data.repository.HomeRepository
import com.android04.godfisherman.data.repository.LocationRepository
import com.android04.godfisherman.utils.LocationHelper
import com.android04.godfisherman.utils.RepoResponseImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val locationRepository: LocationRepository,
    private val locationHelper: LocationHelper
) : ViewModel() {

    private val _currentLocation: MutableLiveData<Location> by lazy { MutableLiveData<Location>() }
    val currentLocation: LiveData<Location> = _currentLocation

    private val _address: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val address: LiveData<String> = _address

    private val _youtubeList: MutableLiveData<List<HomeRecommendData>> by lazy { MutableLiveData<List<HomeRecommendData>>() }
    val youtubeList: LiveData<List<HomeRecommendData>> = _youtubeList

    private val _isYoutubeLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isYoutubeLoading: LiveData<Boolean> = _isYoutubeLoading

    private val _isYoutubeSuccess: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isYoutubeSuccess: LiveData<Boolean> = _isYoutubeSuccess
    
    fun updateLocation() {
        locationHelper.setLocationUpdate()
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val location = locationHelper.getLocation()
                _currentLocation.postValue(location)
                _address.postValue(locationRepository.updateLocation(location))
            }
        }
    }

    fun fetchYoutube() {
        viewModelScope.launch {
            _isYoutubeLoading.value = true
            val repoCallback = RepoResponseImpl<List<HomeRecommendData>>()

            repoCallback.addSuccessCallback {
                _youtubeList.postValue(it)
                _isYoutubeSuccess.postValue(true)
                _isYoutubeLoading.postValue(false)
            }

            repoCallback.addFailureCallback {
                _isYoutubeSuccess.postValue(false)
            }

            homeRepository.fetchYoutubeData(repoCallback)
        }
    }

    fun fetchWeather() {
        val location = currentLocation.value

        if (location != null) {
            viewModelScope.launch {
                homeRepository.fetchWeatherData(location.latitude, location.longitude)
            }
        }
    }
}

package com.android04.godfisherman.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android04.godfisherman.data.repository.LocationRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    fun diTest() {
        locationRepository.test()
    }
}

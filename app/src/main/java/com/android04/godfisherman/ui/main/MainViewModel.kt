package com.android04.godfisherman.ui.main

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android04.godfisherman.common.Result
import com.android04.godfisherman.data.repository.LocationRepository
import com.android04.godfisherman.data.repository.StopwatchRepository
import com.android04.godfisherman.localdatabase.entity.TmpFishingRecord
import com.android04.godfisherman.utils.LocationHelper
import com.android04.godfisherman.utils.StopwatchManager
import com.android04.godfisherman.utils.toTimeMilliSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: StopwatchRepository,
    private val locationRepository: LocationRepository,
    private val locationHelper: LocationHelper
) : ViewModel() {
    companion object {
        var isTimeLine = false
        var isFromService = false
    }
    val stopwatch = StopwatchManager({ time -> _displayTime.postValue(time.toTimeMilliSecond()) })
    val stopwatchOnFlag: MutableLiveData<Boolean> = MutableLiveData(false)
    var beforeMenuItemId: Int = 0
    var isFromStopwatchFragment: Boolean = false
    var isFromInfoFragment: Boolean = false
    var isOpened: Boolean = false
    var isServiceRequestWithOutCamera = true

    var lastBackTime = 0L

    private val _currentLocation: MutableLiveData<Location?> by lazy {
        MutableLiveData<Location?>(
            null
        )
    }
    val currentLocation: LiveData<Location?> = _currentLocation

    private val _isStopwatchStarted: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(
            false
        )
    }
    val isStopwatchStarted: LiveData<Boolean> = _isStopwatchStarted

    private val _isAfterUpload: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val isAfterUpload: LiveData<Boolean> = _isAfterUpload

    private val _tmpFishingList: MutableLiveData<List<TmpFishingRecord>> by lazy { MutableLiveData<List<TmpFishingRecord>>() }
    val tmpFishingList: LiveData<List<TmpFishingRecord>> = _tmpFishingList

    private val _displayTime: MutableLiveData<String> by lazy { MutableLiveData<String>("00:00:00.00") }
    val displayTime: LiveData<String> = _displayTime

    fun startOrStopTimer(): Boolean {
        return if (isStopwatchStarted.value == true) {
            endStopwatch()
            true
        } else {
            startStopwatch()
            false
        }
    }

    fun passedTimeFromService(passedTime: Double) {
        stopwatch.start(10, passedTime)
        _isStopwatchStarted.value = true
    }

    private fun startStopwatch() {
        isTimeLine = true
        stopwatch.start(10)
        _isStopwatchStarted.value = true
    }

    fun endStopwatch() {
        isTimeLine = false
        stopwatch.end()
        _isStopwatchStarted.value = false
    }

    fun resumeStopwatch() {
        isTimeLine = true
        stopwatch.resumeStopwatch(10)
        _isStopwatchStarted.value = true
    }

    fun saveTimeLineRecord() {
        if (!_tmpFishingList.value.isNullOrEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                when (repository.saveTimeLineRecord(stopwatch.getTime())) {
                    is Result.Success -> {
                        //todo
                    }
                    is Result.Fail -> {
                        //todo
                    }
                }
            }
        }
        _isAfterUpload.value = true
    }

    fun loadTmpTimeLineRecord() {
        viewModelScope.launch(Dispatchers.IO) {
            _tmpFishingList.postValue(repository.loadTmpTimeLineRecord())
        }
    }

    fun passStopwatchToService() {
        _isStopwatchStarted.value = false
        stopwatch.end()
    }

    fun setIsAfterUploadFalse() {
        _isAfterUpload.value = false
    }

    fun requestLocation() {
        locationHelper.setLocationUpdate { updateLocation() }
    }

    private fun updateLocation() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val location = locationHelper.getLocation()
                locationRepository.saveLocation(location)
                _currentLocation.postValue(location)
            }
        }
    }

}

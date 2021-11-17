package com.android04.godfisherman.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android04.godfisherman.common.NetworkChecker
import com.android04.godfisherman.data.repository.StopwatchRepository
import com.android04.godfisherman.localdatabase.entity.TmpFishingRecord
import com.android04.godfisherman.utils.toTimeMilliSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: StopwatchRepository
) : ViewModel() {
    companion object{
        var isTimeLine = false
        var isFromService = false
    }
    val stopwatchOnFlag: MutableLiveData<Boolean> = MutableLiveData(false)
    var beforeMenuItemId: Int = 0
    var isFromStopwatchFragment: Boolean = false
    var isFromInfoFragment: Boolean = false
    var isOpened: Boolean = false
    var isServiceRequestWithOutCamera = true

    var lastBackTime = 0L

    private val _isNetworkConnected: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isNetworkConnected: LiveData<Boolean> = _isNetworkConnected

    fun checkConnectivity() {
        //_isNetworkConnected.value = NetworkChecker.isConnected()
    }

    private val _isStopwatchStarted: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val isStopwatchStarted: LiveData<Boolean> = _isStopwatchStarted

    private val _isAfterUpload: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val isAfterUpload: LiveData<Boolean> = _isAfterUpload

    private val _tmpFishingList: MutableLiveData<List<TmpFishingRecord>> by lazy{ MutableLiveData<List<TmpFishingRecord>>() }
    val tmpFishingList: LiveData<List<TmpFishingRecord>> = _tmpFishingList

    private lateinit var stopwatch: Timer
    var time = 0.0
    var resumeTime = 0.0

    private val _displayTime: MutableLiveData<String> by lazy { MutableLiveData<String>("00:00:00.00") }
    val displayTime: LiveData<String> = _displayTime

    fun startOrStopTimer(): Boolean{
        return if(isStopwatchStarted.value == true) {
            endStopwatch()
            true
        } else {
            startStopwatch()
            false
        }
    }

    fun passedTimeFromService(passedTime: Double){
        time = passedTime
        stopwatch = Timer()
        stopwatch.scheduleAtFixedRate(StopwatchTask(), 0, 10)
        _isStopwatchStarted.value = true
    }

    fun resetStopwatch(){
        time = 0.0
        _displayTime.postValue(time.toTimeMilliSecond())
    }

    private fun startStopwatch(){
        Log.d("UploadDialog", "startStopwatch()")
        isTimeLine = true
        stopwatch = Timer()
        stopwatch.scheduleAtFixedRate(StopwatchTask(), 0, 10)
        _isStopwatchStarted.value = true
    }

    fun endStopwatch(){
        Log.d("UploadDialog", "endStopwatch()")
        isTimeLine = false
        stopwatch.cancel()
        resumeTime = time
        _isStopwatchStarted.value = false
    }

    fun resumeStopwatch(){
        Log.d("UploadDialog", "resumeStopwatch()")
        isTimeLine = true
        stopwatch = Timer()
        time = resumeTime
        stopwatch.scheduleAtFixedRate(StopwatchTask(), 0, 10)
        _isStopwatchStarted.value = true
    }

    fun saveTimeLineRecord(){
        if (!_tmpFishingList.value.isNullOrEmpty()){
            viewModelScope.launch(Dispatchers.IO){
                repository.saveTimeLineRecord(time)
            }
        }
        _isAfterUpload.value = true
    }

    private inner class StopwatchTask() : TimerTask() {
        override fun run() {
            time++
            Log.d("StopWatch", "로컬에서 타이머 실행 중 $time")
            _displayTime.postValue(time.toTimeMilliSecond())
        }

        override fun cancel(): Boolean {
            resumeTime = time
            time = 0.0
            return super.cancel()
        }
    }

    fun loadTmpTimeLineRecord(){
        viewModelScope.launch(Dispatchers.IO) {
            _tmpFishingList.postValue(repository.loadTmpTimeLineRecord())
        }
    }

    fun passStopwatchToService(){
        _isStopwatchStarted.value = false
        stopwatch.cancel()
    }

    fun setIsAfterUploadFalse(){
        _isAfterUpload.value = false
    }

}

package com.android04.godfisherman.ui.stopwatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android04.godfisherman.data.repository.StopwatchRepository
import com.android04.godfisherman.localdatabase.entity.TmpFishingRecord
import com.android04.godfisherman.utils.toTimeMilliSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import java.util.*
import javax.inject.Inject

@HiltViewModel
class StopwatchViewModel @Inject constructor(
    private val repository: StopwatchRepository
): ViewModel() {

    companion object{
        var isTimeLine = false
    }

    private val _isStopwatchStarted: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val isStopwatchStarted: LiveData<Boolean> = _isStopwatchStarted

    private val _tmpFishingList: MutableLiveData<List<TmpFishingRecord>> by lazy{ MutableLiveData<List<TmpFishingRecord>>() }
    val tmpFishingList: LiveData<List<TmpFishingRecord>> = _tmpFishingList

    private lateinit var stopwatch: Timer
    var time = 0.0

    private val _displayTime = MutableLiveData<String>("00:00:00.00")
    val displayTime: LiveData<String> = _displayTime

    fun startOrStopTimer(){
        if(isStopwatchStarted.value == true) {
            endStopwatch()
        } else {
            startStopwatch()
        }
    }

    fun passedTimeFromService(passedTime: Double){
        time = passedTime
        stopwatch = Timer()
        stopwatch.scheduleAtFixedRate(StopwatchTask(), 0, 10)
        _isStopwatchStarted.value = true
    }

    private fun startStopwatch(){
        isTimeLine = true
        stopwatch = Timer()
        stopwatch.scheduleAtFixedRate(StopwatchTask(), 0, 10)
        _isStopwatchStarted.value = true
    }

    private fun endStopwatch(){
        isTimeLine = false
        viewModelScope.launch(Dispatchers.IO){
            saveTimeLineRecord()
        }
        _isStopwatchStarted.value = false
        stopwatch.cancel()
        sleep(100)
        time = 0.0
        _displayTime.value = time.toTimeMilliSecond()
        _isStopwatchStarted.value = false
    }

    private suspend fun saveTimeLineRecord(){
        repository.saveTimeLineRecord(time)
    }

    private inner class StopwatchTask() : TimerTask() {
        override fun run() {
            time++
            _displayTime.postValue(time.toTimeMilliSecond())
        }
    }

    fun loadTmpTimeLineRecord(){
        viewModelScope.launch(Dispatchers.IO) {
            _tmpFishingList.postValue(repository.loadTmpTimeLineRecord())
        }
    }
}

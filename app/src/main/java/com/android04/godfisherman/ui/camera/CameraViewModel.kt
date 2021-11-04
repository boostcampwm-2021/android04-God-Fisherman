package com.android04.godfisherman.ui.camera

import android.hardware.SensorEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.abs
import kotlin.math.roundToInt

class CameraViewModel : ViewModel(){
    private val _isLevelOk = MutableLiveData<Boolean>(false)
    val isLevelOk: LiveData<Boolean> = _isLevelOk

    fun changedLevel(event: SensorEvent){
        _isLevelOk.value = abs(event.values[0].roundToInt()) <= 1 && abs(event.values[1].roundToInt()) <= 1
    }

}
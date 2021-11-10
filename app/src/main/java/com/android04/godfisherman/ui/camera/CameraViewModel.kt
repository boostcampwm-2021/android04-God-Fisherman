package com.android04.godfisherman.ui.camera

import android.hardware.SensorEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android04.godfisherman.utils.MONEY_SIZE
import com.android04.godfisherman.utils.convertCentiMeter
import com.android04.godfisherman.utils.findHeightCenter
import kotlin.math.abs
import kotlin.math.roundToInt

class CameraViewModel : ViewModel() {
    private val _isLevelOk = MutableLiveData<Boolean>(false)
    val isLevelOk: LiveData<Boolean> = _isLevelOk

    private val _fishRect = MutableLiveData<List<Int>?>()
    val fishRect : LiveData<List<Int>?> = _fishRect

    private val _moneyRect = MutableLiveData<List<Int>?>()
    val moneyRect : LiveData<List<Int>?> = _moneyRect

    private val _bodySize = MutableLiveData<String?>()
    val bodySize : LiveData<String?> = _bodySize

    fun setRect(list: List<List<Int>>) {
        when {
            list.size >= 2 -> {
                if (findHeightCenter(list[0]) >= findHeightCenter(list[1])) {
                    _fishRect.value = list[1]
                    _moneyRect.value = list[0]
                } else {
                    _fishRect.value = list[0]
                    _moneyRect.value = list[1]
                }
            }
            list.size == 1 -> {
                _fishRect.value = list[0]
                _moneyRect.value = null
            }
            list.isEmpty() -> {
                _fishRect.value = null
                _moneyRect.value = null
            }
        }
    }

    fun setSize(list: List<Int>) {
        if (list.size >= 2) {
            _bodySize.value = convertCentiMeter(list[0].toFloat() / list[1] * MONEY_SIZE)
        } else {
            _bodySize.value = null
        }
    }
    
    fun changedLevel(event: SensorEvent){
        _isLevelOk.value = abs(event.values[0].roundToInt()) <= 1 && abs(event.values[1].roundToInt()) <= 1
    }
}
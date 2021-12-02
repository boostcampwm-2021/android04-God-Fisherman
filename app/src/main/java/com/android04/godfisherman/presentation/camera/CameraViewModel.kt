package com.android04.godfisherman.presentation.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android04.godfisherman.utils.findHeightCenter
import com.android04.godfisherman.utils.heightConvert
import com.android04.godfisherman.utils.isLevelCorrect
import com.android04.godfisherman.utils.widthConvert

class CameraViewModel : ViewModel() {
    private val _isLevelOk: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val isLevelOk: LiveData<Boolean> = _isLevelOk

    private val _fishRect: MutableLiveData<List<Int>?> by lazy { MutableLiveData<List<Int>?>() }
    val fishRect: LiveData<List<Int>?> = _fishRect

    private val _moneyRect: MutableLiveData<List<Int>?> by lazy { MutableLiveData<List<Int>?>() }
    val moneyRect: LiveData<List<Int>?> = _moneyRect

    private val _bodySize: MutableLiveData<Double?> by lazy { MutableLiveData<Double?>() }
    val bodySize: LiveData<Double?> = _bodySize

    private val _isShutterPressed: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val isShutterPressed: LiveData<Boolean> = _isShutterPressed

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

        calculateSize()
    }

    private fun calculateSize() {
        val fishRect = _fishRect.value
        val moneyRect = _moneyRect.value

        if (fishRect != null && moneyRect != null) {
            val fishSize = fishRect[2] - fishRect[3]
            val moneySize = moneyRect[2] - moneyRect[3]

            _bodySize.value = fishSize.toDouble() / moneySize * MONEY_SIZE
        } else {
            _bodySize.value = null
        }
    }

    fun getCropRect(curWidth: Int, curHeight: Int, imgWidth: Int, imgHeight: Int): List<Int>? {
        val rect = fishRect.value

        return if (rect != null) {
            listOf(
                heightConvert(rect[0], curHeight, imgHeight),
                heightConvert(rect[1], curHeight, imgHeight),
                widthConvert(rect[2], curWidth, imgWidth),
                widthConvert(rect[3], curWidth, imgWidth)
            )
        } else {
            null
        }
    }

    fun changedLevel(x: Float, y: Float) {
        _isLevelOk.value = isLevelCorrect(x, y)
    }

    fun setShutterPressed(pressed: Boolean) {
        _isShutterPressed.value = pressed
    }

    companion object {
        const val MONEY_SIZE = 13.6F
    }
}

package com.android04.godfisherman.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val stopwatchOnFlag: MutableLiveData<Boolean> = MutableLiveData(false)
    var beforeMenuItemId: Int = 0
    var isFromStopwatchFragment: Boolean = false
    var isFromInfoFragment: Boolean = false
}

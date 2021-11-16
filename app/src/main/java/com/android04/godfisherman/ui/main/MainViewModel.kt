package com.android04.godfisherman.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android04.godfisherman.common.NetworkChecker

class MainViewModel : ViewModel() {
  
    val stopwatchOnFlag: MutableLiveData<Boolean> = MutableLiveData(false)
    var beforeMenuItemId: Int = 0
    var isFromStopwatchFragment: Boolean = false
    var isFromInfoFragment: Boolean = false
    var isOpened: Boolean = false

    private val _isNetworkConnected: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isNetworkConnected: LiveData<Boolean> = _isNetworkConnected

    fun checkConnectivity() {
        _isNetworkConnected.value = NetworkChecker.isConnected()
    }
    
}

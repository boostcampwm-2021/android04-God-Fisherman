package com.android04.godfisherman.ui.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android04.godfisherman.utils.SharedPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(private val manager: SharedPreferenceManager) :
    ViewModel() {
    private val _isFirstStart: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isFirstStart: LiveData<Boolean> = _isFirstStart

    fun fetchFirstStart() {
        _isFirstStart.value = manager.getString(FIRST_START) != FIRST_START
    }

    fun setFirstStart() {
        manager.saveString(FIRST_START, FIRST_START)
    }

    companion object {
        const val FIRST_START = "FIRST_START"
    }
}
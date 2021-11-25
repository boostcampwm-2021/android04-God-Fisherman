package com.android04.godfisherman.ui.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android04.godfisherman.data.repository.IntroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(private val repository: IntroRepository) :
    ViewModel() {
    private val _isFirstStart: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isFirstStart: LiveData<Boolean> = _isFirstStart

    fun fetchFirstStart() {
        _isFirstStart.value = repository.fetchFirstStart()
    }

    fun setFirstStart() {
        repository.setFirstStart()
    }

    companion object {
        const val FIRST_START = "FIRST_START"
    }
}
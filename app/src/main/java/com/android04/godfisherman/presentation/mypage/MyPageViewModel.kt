package com.android04.godfisherman.presentation.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android04.godfisherman.data.repository.LogInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val repository: LogInRepository
) : ViewModel() {

    private val _userInfo: MutableLiveData<UserInfo> by lazy { MutableLiveData<UserInfo>() }
    val userInfo: LiveData<UserInfo> = _userInfo

    fun fetchUserData() {
        _userInfo.value = repository.getUserInfo()
    }

    fun doLogout() {
        repository.doLogOut()
    }
}
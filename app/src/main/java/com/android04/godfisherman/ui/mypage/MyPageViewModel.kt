package com.android04.godfisherman.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android04.godfisherman.data.repository.LogInRepository
import com.android04.godfisherman.utils.RepoResponseImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val repository: LogInRepository
) : ViewModel() {

    private val _userInfo : MutableLiveData<UserInfo> by lazy { MutableLiveData<UserInfo>() }
    val userInfo : LiveData<UserInfo> = _userInfo

    private val _isSignOutSuccess : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isSignOutSuccess : LiveData<Boolean> = _isSignOutSuccess

    fun fetchUserData() {
        _userInfo.value = repository.getUserInfo()
    }

    fun doLogout() {
        repository.doLogOut()
    }

    fun doSignOut() {
        val callback = RepoResponseImpl<Unit>()

        callback.addSuccessCallback {
            _isSignOutSuccess.postValue(true)
        }

        callback.addFailureCallback {
            _isSignOutSuccess.postValue(false)
        }

        repository.doSignOut(callback)
    }
}
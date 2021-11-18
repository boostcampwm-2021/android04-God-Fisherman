package com.android04.godfisherman.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android04.godfisherman.ui.login.LogInViewModel
import com.android04.godfisherman.utils.SharedPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(private val manager: SharedPreferenceManager) : ViewModel() {

    private val _userName : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val userName : LiveData<String> = _userName

    private val _userEmail : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val userEmail : LiveData<String> = _userEmail

    private val _userImageUrl : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val userImageUrl : LiveData<String> = _userImageUrl

    fun fetchUserData() {
        _userName.value = manager.getString(LogInViewModel.LOGIN_NAME)
        _userEmail.value = manager.getString(LogInViewModel.LOGIN_EMAIL)
        _userImageUrl.value = manager.getString(LogInViewModel.LOGIN_IMG)
    }

    fun doLogout() {
        manager.deleteString(LogInViewModel.LOGIN_NAME)
        manager.deleteString(LogInViewModel.LOGIN_EMAIL)
        manager.deleteString(LogInViewModel.LOGIN_IMG)
    }
}
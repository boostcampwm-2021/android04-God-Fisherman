package com.android04.godfisherman.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android04.godfisherman.data.repository.LogInRepository
import com.android04.godfisherman.common.RepoResponseImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val repository: LogInRepository
) : ViewModel() {

    private val _isLogin: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isLogin: LiveData<Boolean> = _isLogin

    private val _isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLogInSuccess: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isLogInSuccess: LiveData<Boolean> = _isLogInSuccess

    fun doLogIn() {
        val callback = RepoResponseImpl<Unit>()
        _isLoading.value = true

        callback.addSuccessCallback {
            _isLogInSuccess.postValue(true)
            _isLoading.postValue(false)
        }

        callback.addFailureCallback {
            _isLogInSuccess.postValue(false)
            _isLoading.postValue(false)
        }

        repository.doLogIn(callback)
    }

    fun fetchLoginData() {
        _isLogin.value = repository.isAutoLogIn()
    }

    fun setLoginData(token: String, name: String, mail: String, img: String) {
        repository.setLogInData(token, name, mail, img)
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    companion object {
        const val LOGIN_TOKEN = "LOGIN_TOKEN"
        const val LOGIN_NAME = "LOGIN_NAME"
        const val LOGIN_EMAIL = "LOGIN_MAIL"
        const val LOGIN_IMG = "LOGIN_IMG"
    }
}
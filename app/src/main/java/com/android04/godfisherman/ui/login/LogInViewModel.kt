package com.android04.godfisherman.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android04.godfisherman.utils.SharedPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(private val manager: SharedPreferenceManager) : ViewModel() {

    private val _isLogin: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isLogin: LiveData<Boolean> = _isLogin

    private val _isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchLoginData() {
        val name = manager.getString(LOGIN_NAME)
        val email = manager.getString(LOGIN_EMAIL)

        _isLogin.value = name != "" && email != ""
    }

    fun setLoginData(name: String, mail: String) {
        manager.saveString("LOGIN_NAME", name)
        manager.saveString("LOGIN_MAIL", mail)
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    companion object {
        const val LOGIN_NAME = "LOGIN_NAME"
        const val LOGIN_EMAIL = "LOGIN_MAIL"
    }
}
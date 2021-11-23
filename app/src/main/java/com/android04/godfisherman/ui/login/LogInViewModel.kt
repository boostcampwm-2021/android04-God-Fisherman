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
        val token = manager.getString(LOGIN_TOKEN)
        val name = manager.getString(LOGIN_NAME)
        val email = manager.getString(LOGIN_EMAIL)
        val img = manager.getString(LOGIN_IMG)

        _isLogin.value = token != "" && name != "" && email != "" && img != null
    }

    fun setLoginData(token: String, name: String, mail: String, img: String) {
        manager.saveString(LOGIN_NAME, name)
        manager.saveString(LOGIN_EMAIL, mail)
        manager.saveString(LOGIN_IMG, img)
        manager.saveString(LOGIN_TOKEN, token)
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
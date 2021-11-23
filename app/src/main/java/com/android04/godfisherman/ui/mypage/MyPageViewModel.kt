package com.android04.godfisherman.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android04.godfisherman.ui.login.LogInViewModel
import com.android04.godfisherman.utils.SharedPreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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

    private val _isSignOutSuccess : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isSignOutSuccess : LiveData<Boolean> = _isSignOutSuccess

    fun fetchUserData() {
        _userName.value = manager.getString(LogInViewModel.LOGIN_NAME)
        _userEmail.value = manager.getString(LogInViewModel.LOGIN_EMAIL)
        _userImageUrl.value = manager.getString(LogInViewModel.LOGIN_IMG)
    }

    fun doLogout() {
        manager.deleteString(LogInViewModel.LOGIN_NAME)
        manager.deleteString(LogInViewModel.LOGIN_EMAIL)
        manager.deleteString(LogInViewModel.LOGIN_IMG)
        manager.deleteString(LogInViewModel.LOGIN_TOKEN)
    }

    fun doSignOut() {
        val idToken = manager.getString(LogInViewModel.LOGIN_TOKEN)
        val auth = FirebaseAuth.getInstance()

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                auth.currentUser!!.delete()
                    .addOnSuccessListener {
                        _isSignOutSuccess.postValue(true)
                    }
                    .addOnFailureListener {
                        _isSignOutSuccess.postValue(false)
                    }
            }
            .addOnFailureListener {
                _isSignOutSuccess.postValue(false)
            }
    }
}
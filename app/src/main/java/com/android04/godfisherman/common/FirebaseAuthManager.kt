package com.android04.godfisherman.common

import com.android04.godfisherman.presentation.login.LogInViewModel.Companion.LOGIN_EMAIL
import com.android04.godfisherman.presentation.login.LogInViewModel.Companion.LOGIN_IMG
import com.android04.godfisherman.presentation.login.LogInViewModel.Companion.LOGIN_NAME
import com.android04.godfisherman.presentation.login.LogInViewModel.Companion.LOGIN_TOKEN
import com.android04.godfisherman.presentation.mypage.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject

class FirebaseAuthManager @Inject constructor(
    private val manager: SharedPreferenceManager
) {

    private val auth = FirebaseAuth.getInstance()

    fun isAutoLogIn(): Boolean {
        val token = manager.getString(LOGIN_TOKEN)
        val name = manager.getString(LOGIN_NAME)
        val email = manager.getString(LOGIN_EMAIL)
        val img = manager.getString(LOGIN_IMG)

        return token != "" && name != "" && email != "" && img != null
    }

    fun setLogInToken(token: String) {
        manager.saveString(LOGIN_TOKEN, token)
    }

    fun setUserInfo(userInfo: UserInfo) {
        manager.saveString(LOGIN_NAME, userInfo.name)
        manager.saveString(LOGIN_EMAIL, userInfo.email)
        manager.saveString(LOGIN_IMG, userInfo.imgUrl)
    }

    fun getUserInfo(): UserInfo {
        val name = manager.getString(LOGIN_NAME) ?: ""
        val email = manager.getString(LOGIN_EMAIL) ?: ""
        val img = manager.getString(LOGIN_IMG) ?: ""

        return UserInfo(name, email, img)
    }

    fun doLogIn(callback: RepoResponse<Unit>) {
        val token = manager.getString(LOGIN_TOKEN)
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                callback.invoke(true, Unit)
            }
            .addOnFailureListener {
                callback.invoke(false, Unit)
            }
    }

    fun doLogOut() {
        auth.signOut()
        manager.deleteString(LOGIN_TOKEN)
        manager.deleteString(LOGIN_NAME)
        manager.deleteString(LOGIN_EMAIL)
        manager.deleteString(LOGIN_IMG)
    }

}
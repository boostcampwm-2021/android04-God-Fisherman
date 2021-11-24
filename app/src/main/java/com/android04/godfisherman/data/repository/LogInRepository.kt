package com.android04.godfisherman.data.repository

import com.android04.godfisherman.ui.mypage.UserInfo
import com.android04.godfisherman.utils.FirebaseAuthManager
import com.android04.godfisherman.utils.RepoResponse
import javax.inject.Inject

class LogInRepository @Inject constructor(
    private val manager: FirebaseAuthManager
) {

    fun setLogInData(token: String, name: String, email: String, img: String) {
        manager.setLogInToken(token)
        manager.setUserInfo(UserInfo(name, email, img))
    }

    fun doLogIn(callback: RepoResponse<Unit>) {
        manager.doLogIn(callback)
    }

    fun doLogOut() {
        manager.doLogOut()
    }

    fun doSignOut(callback: RepoResponse<Unit>) {
        manager.doSignOut(callback)
    }

    fun getUserInfo() = manager.getUserInfo()

    fun isAutoLogIn() = manager.isAutoLogIn()
}
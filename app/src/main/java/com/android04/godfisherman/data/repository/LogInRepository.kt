package com.android04.godfisherman.data.repository

import com.android04.godfisherman.common.FirebaseAuthManager
import com.android04.godfisherman.common.RepoResponse
import com.android04.godfisherman.presentation.mypage.UserInfo
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

    fun loadUserInfo() = manager.loadUserInfo()

    fun isAutoLogIn() = manager.isAutoLogIn()
}

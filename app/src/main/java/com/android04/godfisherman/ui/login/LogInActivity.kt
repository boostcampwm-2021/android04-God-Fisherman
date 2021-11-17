package com.android04.godfisherman.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.ActivityLogInBinding
import com.android04.godfisherman.ui.base.BaseActivity
import com.android04.godfisherman.ui.intro.GodFishermanIntro
import com.android04.godfisherman.utils.showToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInActivity : BaseActivity<ActivityLogInBinding, LogInViewModel>(R.layout.activity_log_in) {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override val viewModel: LogInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setLoginInstance()
        setListener()
        setObserver()

        viewModel.fetchLoginData()
    }

    private fun setLoginInstance() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()
    }

    private fun setListener() {
        binding.googleButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    private fun setObserver() {
        viewModel.isLogin.observe(this) {
            if (it) {
                showToast(this, "자동 로그인 되었습니다.")
                moveToIntro()
            }
        }
    }

    private fun moveToIntro() {
        val intent = Intent(this, GodFishermanIntro::class.java)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                showToast(this, "인증에 실패했습니다. 잠시 후 다시 시도해주세요.")
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val idToken = account.idToken
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    showToast(this, "구글 로그인에 성공하였습니다.")
                    viewModel.setLoginData(account.displayName!!, account.email!!)
                    moveToIntro()
                } else {
                    showToast(this, "인증에 실패했습니다. 잠시 후 다시 시도해주세요.")
                }
            }
    }

    companion object {
        const val RC_SIGN_IN = 999
    }
}
package com.android04.godfisherman.ui.login

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
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
    private lateinit var loadingDialog: Dialog

    override val viewModel: LogInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setLoginInstance()
        setListener()
        setObserver()
        setLoadingDialog()

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
            viewModel.setLoading(true)
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
        viewModel.isLoading.observe(this) {
            if (it) {
                showLoadingDialog()
            } else {
                cancelLoadingDialog()
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
                viewModel.setLoading(false)
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
                    viewModel.setLoading(false)
                    showToast(this, "구글 로그인에 성공하였습니다.")
                    viewModel.setLoginData(account.displayName!!, account.email!!, account.photoUrl!!.toString())
                    moveToIntro()
                } else {
                    viewModel.setLoading(false)
                    showToast(this, "인증에 실패했습니다. 잠시 후 다시 시도해주세요.")
                }
            }
    }

    private fun setLoadingDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.setContentView(R.layout.dialog_upload_loading)

        loadingDialog = dialog
    }

    private fun showLoadingDialog() {
        if (::loadingDialog.isInitialized) {
            loadingDialog.show()
        }
    }

    private fun cancelLoadingDialog() {
        if (::loadingDialog.isInitialized) {
            loadingDialog.cancel()
        }
    }

    companion object {
        const val RC_SIGN_IN = 999
    }
}
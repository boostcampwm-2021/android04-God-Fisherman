package com.android04.godfisherman.ui.login

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import androidx.activity.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.ActivityLogInBinding
import com.android04.godfisherman.presentation.login.LogInViewModel
import com.android04.godfisherman.ui.base.BaseActivity
import com.android04.godfisherman.ui.intro.GodFishermanIntro
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInActivity : BaseActivity<ActivityLogInBinding, LogInViewModel>(R.layout.activity_log_in) {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var loadingDialog: Dialog

    override val viewModel: LogInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFullScreen()
        setLoginInstance()
        setListener()
        setObserver()
        setLoadingDialog()

        viewModel.fetchLoginData()
    }

    private fun setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
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
                showToast(this, R.string.login_auto)
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
        viewModel.isLogInSuccess.observe(this) {
            when(it) {
                true -> {
                    showToast(this, R.string.login_success)
                    moveToIntro()
                }
                false -> {
                    showToast(this, R.string.login_server_fail)
                }
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
                viewModel.setLoginData(
                    account.idToken!!,
                    account.displayName!!,
                    account.email!!,
                    account.photoUrl!!.toString()
                )
                viewModel.doLogIn()
            } catch (e: ApiException) {
                viewModel.setLoading(false)
                showToast(this, R.string.login_google_fail)
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
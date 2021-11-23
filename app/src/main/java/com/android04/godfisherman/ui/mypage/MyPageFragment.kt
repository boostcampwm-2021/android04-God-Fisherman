package com.android04.godfisherman.ui.mypage

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.fragment.app.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.FragmentMyPageBinding
import com.android04.godfisherman.ui.base.BaseFragment
import com.android04.godfisherman.ui.login.LogInActivity
import com.android04.godfisherman.utils.showToast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding, MyPageViewModel>(R.layout.fragment_my_page) {

    override val viewModel: MyPageViewModel by viewModels()

    private lateinit var loadingDialog : Dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        setStatusBarColor(R.color.background_home)
        setLoadingDialog()
        setListener()
        setObserver()

        viewModel.fetchUserData()
    }

    private fun setObserver() {
        viewModel.isSignOutSuccess.observe(viewLifecycleOwner) {
            when(it) {
                true -> {
                    showToast(requireContext(), R.string.signout_success)
                    viewModel.doLogout()
                    moveToLogIn()
                }
                false -> {
                    showToast(requireContext(), R.string.signout_fail)
                }
            }
            cancelLoadingDialog()
        }
    }

    private fun setListener() {
        binding.btLogout.setOnClickListener {
            logOut()
        }
        binding.btSignOut.setOnClickListener {
            signOut()
        }
    }

    private fun logOut() {
        FirebaseAuth.getInstance().signOut()
        showToast(requireContext(), R.string.logout_success)
        viewModel.doLogout()
        moveToLogIn()
    }

    private fun signOut() {
        showLoadingDialog()
        viewModel.doSignOut()
    }

    private fun moveToLogIn() {
        val intent = Intent(activity, LogInActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun setLoadingDialog() {
        val dialog = Dialog(requireContext())
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
}

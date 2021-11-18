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
        setLoadingDialog()
        setListener()

        viewModel.fetchUserData()
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
        showToast(requireContext(), "로그 아웃하였습니다.")
        viewModel.doLogout()
        moveToLogIn()
    }

    private fun signOut() {
        showLoadingDialog()
        FirebaseAuth.getInstance().currentUser?.delete()?.addOnSuccessListener {
            showToast(requireContext(), "성공적으로 탈퇴하였습니다.")
            viewModel.doLogout()
            moveToLogIn()
        }?.addOnCompleteListener {
            cancelLoadingDialog()
        }
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
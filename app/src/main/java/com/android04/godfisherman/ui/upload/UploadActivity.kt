package com.android04.godfisherman.ui.upload

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.common.LoadingDialogProvider
import com.android04.godfisherman.databinding.ActivityUploadBinding
import com.android04.godfisherman.presentation.upload.UploadViewModel
import com.android04.godfisherman.ui.base.BaseActivity
import com.android04.godfisherman.ui.camera.CameraActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadActivity :
    BaseActivity<ActivityUploadBinding, UploadViewModel>(R.layout.activity_upload) {

    override val viewModel: UploadViewModel by viewModels()

    private val loadingDialog: Dialog by lazy {
        LoadingDialogProvider().provideLoadingDialog(this, R.layout.dialog_upload_loading)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setOrientation()
        initListener()
        setupObserver()
        setUpBinding()
        loadData()

    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun setOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun setUpBinding() {
        binding.uploadViewModel = viewModel
    }

    private fun loadData() {
        val size = intent.getDoubleExtra(CameraActivity.INTENT_FISH_SIZE, 0.0)
        viewModel.fetchInitData(size)
    }

    private fun setupObserver() {
        viewModel.fishTypeList.observe(this) {
            val adapter = ArrayAdapter(
                binding.autoCompleteTextviewFishType.context,
                R.layout.item_fish_type,
                it
            )

            binding.autoCompleteTextviewFishType.setAdapter(adapter)
        }
        viewModel.isFetchSuccess.observe(this) {
            it?.let {
                when (it) {
                    true -> {
                        binding.toolbarTop.menu.getItem(0).isEnabled = true
                    }
                    false -> {
                        binding.toolbarTop.menu.getItem(0).isEnabled = false
                        showToast(this, R.string.fetch_fail)
                    }
                }
            }
        }
        viewModel.isUploadSuccess.observe(this) {
            when (it) {
                true -> {
                    showToast(this, R.string.upload_server_success)
                    CameraActivity.captureImage = null
                    finish()
                }
                false -> {
                    showToast(this, R.string.upload_server_fail)
                }
            }

        }
        viewModel.isInputCorrect.observe(this) {
            if (it == false) {
                showToast(this, R.string.upload_input_fail)
            }
        }
        viewModel.isSizeCorrect.observe(this) {
            if (it == false) {
                showToast(this, R.string.upload_size_fail)
            }
        }

        viewModel.isLoading.observe(this) {
            when (it) {
                true -> {
                    showLoadingDialog()
                }
                false -> {
                    cancelLoadingDialog()
                }
            }
        }

        viewModel.isNetworkConnected.observe(this) {
            if (it == false) {
                showToast(this, R.string.upload_network_disconnected)
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initListener() {
        binding.toolbarTop.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun showLoadingDialog() {
        loadingDialog.show()
    }

    private fun cancelLoadingDialog() {
        loadingDialog.cancel()
    }
}

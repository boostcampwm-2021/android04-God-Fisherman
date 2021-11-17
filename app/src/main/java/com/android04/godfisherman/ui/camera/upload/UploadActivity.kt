package com.android04.godfisherman.ui.camera.upload

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.ActivityUploadBinding
import com.android04.godfisherman.ui.base.BaseActivity
import com.android04.godfisherman.ui.camera.CameraActivity
import com.android04.godfisherman.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadActivity :
    BaseActivity<ActivityUploadBinding, UploadViewModel>(R.layout.activity_upload) {

    override val viewModel: UploadViewModel by viewModels()

    private lateinit var loadingDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setOrientation()
        initListener()
        setupObserver()
        setUpBinding()
        setLoadingDialog()
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

        binding.autoCompleteTextviewFishType.setOnClickListener {
            viewModel.fetchFishTypeList()
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
}
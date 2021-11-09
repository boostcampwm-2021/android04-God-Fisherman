package com.android04.godfisherman.ui.camera.upload

import android.content.Intent
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupObserver()
        setUpBinding()
        loadData()

        viewModel.fetchFishTypeList()
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
        viewModel.isUploadSuccess.observe(this) {
            when (it) {
                true -> {
                    showToast(this, "업로드가 완료되었습니다.")
                    finish()
                }
                false -> {
                    showToast(this, "업로드에 실패했습니다. 입력한 정보를 확인해주세요.")
                }
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
        finish()
    }
}
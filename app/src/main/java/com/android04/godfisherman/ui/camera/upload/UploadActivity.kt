package com.android04.godfisherman.ui.camera.upload

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.ActivityUploadBinding
import com.android04.godfisherman.ui.base.BaseActivity
import com.android04.godfisherman.ui.camera.CameraActivity
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
        binding.textInputLayoutFishType
    }
}
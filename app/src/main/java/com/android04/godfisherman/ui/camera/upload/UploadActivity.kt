package com.android04.godfisherman.ui.camera.upload

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.ActivityUploadBinding
import com.android04.godfisherman.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadActivity() :
    BaseActivity<ActivityUploadBinding, UploadViewModel>(R.layout.activity_upload) {

    override val viewModel: UploadViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDropdownAdapter()
    }

    fun initDropdownAdapter() {
        val items = listOf("1", "2", "3", "4")
        val adapter = ArrayAdapter(applicationContext, R.layout.item_fish_type, items)
        binding.autoCompleteTextviewFishType.setAdapter(adapter)
    }
}
package com.android04.godfisherman.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BaseActivity<T : ViewDataBinding, R : ViewModel>(
    @LayoutRes
    private val layoutResID: Int
) : AppCompatActivity() {
    lateinit var binding: T
    abstract val viewModel: R

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResID)
        binding.lifecycleOwner = this
    }
}

package com.android04.godfisherman.ui.base

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
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

    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showToast(context: Context, @StringRes resourceId: Int) {
        Toast.makeText(context, context.getString(resourceId), Toast.LENGTH_SHORT).show()
    }
}

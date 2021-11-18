package com.android04.godfisherman.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import android.widget.Button
import com.android04.godfisherman.R

class UploadDialog(context: Context) {
    private val dialog = Dialog(context)

    private lateinit var onUploadClickListener: OnDialogClickListener
    private lateinit var onBackClickListener: OnDialogClickListener

    fun setUploadOnClickListener(listener: OnDialogClickListener) {
        onUploadClickListener = listener
    }

    fun setBackOnClickListener(listener: OnDialogClickListener) {
        onBackClickListener = listener
    }

    fun showDialog() {
        dialog.setContentView(R.layout.dialog_upload)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        dialog.findViewById<Button>(R.id.btn_upload).setOnClickListener {
            onUploadClickListener.onClicked()
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.btn_back).setOnClickListener {
            onBackClickListener.onClicked()
            dialog.dismiss()
        }
    }

    interface OnDialogClickListener {
        fun onClicked()
    }
}
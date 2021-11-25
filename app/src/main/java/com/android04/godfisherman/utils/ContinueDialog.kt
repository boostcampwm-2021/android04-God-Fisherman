package com.android04.godfisherman.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import android.widget.Button
import com.android04.godfisherman.R

class ContinueDialog(context: Context, private val confirmCallback:() -> (Unit), private val cancelCallback:() -> (Unit)) {
    private val dialog = Dialog(context)

    fun showDialog() {
        dialog.setContentView(R.layout.dialog_continue_stopwatch)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        dialog.findViewById<Button>(R.id.btn_confirm).setOnClickListener {
            confirmCallback()
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.btn_back).setOnClickListener {
            cancelCallback()
            dialog.dismiss()
        }
    }

}

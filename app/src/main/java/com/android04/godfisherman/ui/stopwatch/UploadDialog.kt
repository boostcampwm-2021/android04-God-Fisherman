package com.android04.godfisherman.ui.stopwatch

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import android.widget.Button
import com.android04.godfisherman.R

class UploadDialog(context: Context, private val uploadCallback: () -> (Unit), private val backCallback: () -> (Unit)) {
    private val dialog = Dialog(context)

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
        dialog.findViewById<Button>(R.id.btn_confirm).setOnClickListener {
            uploadCallback()
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.btn_back).setOnClickListener {
            backCallback()
            dialog.dismiss()
        }
    }

}

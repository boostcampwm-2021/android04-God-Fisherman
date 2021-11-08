package com.android04.godfisherman.utils

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.android04.godfisherman.R
import com.bumptech.glide.Glide

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("setRect")
    fun setViewWithRect(view: View, rect: List<Int>?) {
        if (rect != null && rect.size == 4) {
            view.top = rect[0]
            view.bottom = rect[1]
            view.left = rect[2]
            view.right = rect[3]
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    @JvmStatic
    @BindingAdapter("setImage")
    fun setImageWithBitmap(view: ImageView, img: Bitmap) {
        // TODO: 에러 상태 이미지 처리 필요
        Glide.with(view.context)
            .load(img)
            .placeholder(R.color.purple_200)
            .error(R.color.money_red)
            .into(view)
    }
}
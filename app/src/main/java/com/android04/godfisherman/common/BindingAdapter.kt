package com.android04.godfisherman.common

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.android04.godfisherman.R
import com.android04.godfisherman.utils.convertCentiMeter
import com.android04.godfisherman.utils.toTimeHourMinute
import com.android04.godfisherman.utils.toTimeString
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import java.util.*

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
    fun setImageWithUrl(view: ImageView, url: String?) {
        Glide.with(view.context)
            .load(url)
            .placeholder(R.drawable.bg_loading_skeleton)
            .error(R.drawable.bg_image_error)
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("setTransparent")
    fun setViewAlphaWithBoolean(view: View, isChecked: Boolean) {
        if (isChecked) {
            view.alpha = 0.3F
        } else {
            view.alpha = 1F
        }
    }

    @JvmStatic
    @BindingAdapter("objectError", "levelError")
    fun setErrorMessage(view: TextView, isObjectCorrect: Boolean, isLevelCorrect: Boolean) {
        when {
            !isLevelCorrect -> {
                view.setText(R.string.level_error)
                view.visibility = View.VISIBLE
            }
            !isObjectCorrect -> {
                view.setText(R.string.object_error)
                view.visibility = View.VISIBLE
            }
            else -> {
                view.visibility = View.INVISIBLE
            }
        }
    }

    @JvmStatic
    @BindingAdapter("setMenuClick")
    fun setOnMenuItemClickListener(toolbar: MaterialToolbar, saveFishingRecord: () -> Unit) {
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.complete -> {
                    saveFishingRecord()
                    true
                }
                else -> false
            }
        }
    }

    @JvmStatic
    @BindingAdapter("setSizeText")
    fun setSizeTextWithDouble(view: TextView, size: Double) {
        view.text = convertCentiMeter(size)
    }

    @JvmStatic
    @BindingAdapter("setTime")
    fun setSizeTextWithDouble(view: TextView, date: Date) {
        view.text = date.toTimeString()
    }

    @JvmStatic
    @BindingAdapter("setTimeToMinute")
    fun setTimeWithInt(view: TextView, millisecond: Int) {
        view.text = millisecond.toTimeHourMinute()
    }

    @JvmStatic
    @BindingAdapter("setImage")
    fun setImage(view: ImageView, image: Bitmap) {
        view.setImageBitmap(image)
    }

    @JvmStatic
    @BindingAdapter("setWelcomeText")
    fun setWelcomeTextWithID(view: TextView, id: String) {
        view.text = "안녕하세요 ${id}님!"
    }

    @JvmStatic
    @BindingAdapter("setVisible")
    fun setVisibleWithBoolean(view: View, isVisible: Boolean?) {
        if (isVisible == true) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.INVISIBLE
        }
    }

    @JvmStatic
    @BindingAdapter("setLottieLoading")
    fun setLottieLoading(view: LottieAnimationView, isLoading: Boolean?) {
        if (isLoading == true) {
            view.playAnimation()
            view.visibility = View.VISIBLE
        } else {
            view.pauseAnimation()
            view.visibility = View.GONE
        }
    }

    @JvmStatic
    @BindingAdapter("setChipLoading")
    fun setChipLoading(view: Chip, isLoading: Boolean?) {
        view.isEnabled = isLoading != true
    }

    @JvmStatic
    @BindingAdapter("setRefreshLoading")
    fun setRefreshLoading(view: SwipeRefreshLayout, isLoading: Boolean?) {
        if (isLoading != true) view.isRefreshing = false
    }

    @JvmStatic
    @BindingAdapter("submitList")
    fun <T> submitListInRecyclerView(recyclerview: RecyclerViewEmptySupport, itemList: List<T>?) {
        itemList?.let {
            recyclerview.submitList(it)
        }
    }

    @JvmStatic
    @BindingAdapter("setLength")
    fun setLength(view: TextView, length: Double) {
        view.text = length.toString() + "cm"
    }

}

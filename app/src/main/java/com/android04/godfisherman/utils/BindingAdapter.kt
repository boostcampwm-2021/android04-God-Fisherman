package com.android04.godfisherman.utils

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.android04.godfisherman.R
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import java.util.*
import kotlin.math.roundToInt

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
    @BindingAdapter("setDate")
    fun setSizeTextWithDouble(view: TextView, date: Date) {
        view.text = date.toDateString()
    }

    @JvmStatic
    @BindingAdapter("setTimeToMinute")
    fun setTimeWithDouble(view: TextView, millisecond: Double) {
        view.text = millisecond.toTimeHourMinute()
    }

    @JvmStatic
    @BindingAdapter("setImage")
    fun setImage(view: ImageView, image: Bitmap) {
        view.setImageBitmap(image)
    }

    @JvmStatic
    @BindingAdapter("visibilityOnMotion")
    fun setVisibilityOnMotion(view: View, visible: Boolean) {
        if (view.parent is MotionLayout) {
            val motionLayout = view.parent as MotionLayout
            val visibility = if (visible) View.VISIBLE else View.GONE

            for (constraintId in motionLayout.constraintSetIds) {
                val constraintSet = motionLayout.getConstraintSet(constraintId)
                constraintSet?.setVisibility(view.id, visibility)
            }
        }
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

    @BindingAdapter("submitList")
    fun <T> submitListInRecyclerView(recyclerview: RecyclerViewEmptySupport, itemList: List<T>?) {
        itemList?.let {
            recyclerview.submitList(it)
        }
    }
}

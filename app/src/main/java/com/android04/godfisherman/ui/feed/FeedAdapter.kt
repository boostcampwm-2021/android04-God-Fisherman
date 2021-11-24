package com.android04.godfisherman.ui.feed

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.ItemFeedPhotoTypeBinding
import com.android04.godfisherman.databinding.ItemFeedTimelineTypeBinding

class FeedAdapter : PagingDataAdapter<FeedData, FeedAdapter.FeedViewHolder>(diffCallback) {
    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        Log.d("pagingAdapter", "onBind 실행")
        val item = getItem(position)
        if (item != null) {
            holder.onBind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {

        Log.d("pagingAdapter", "onCreateViewHolder 실행")
        return when (viewType) {
            PHOTO_TYPE -> {
                val binding = ItemFeedPhotoTypeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FeedPhotoViewHolder(binding)
            }
            else -> {
                val binding = ItemFeedTimelineTypeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FeedTimelineViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is FeedPhotoData -> {
                PHOTO_TYPE
            }
            is FeedTimelineData -> {
                TIMELINE_TYPE
            }
            else -> {
                0
            }
        }
    }

    override fun getItemCount(): Int {
        Log.d("pagingAdapter", "getItemCount 실행: ${super.getItemCount()}")
        return super.getItemCount()
    }

    sealed class FeedViewHolder(binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun onBind(data: FeedData)
    }

    class FeedPhotoViewHolder(private val binding: ItemFeedPhotoTypeBinding) :
        FeedViewHolder(binding) {
        override fun onBind(data: FeedData) {
            val photoData = data as FeedPhotoData
            binding.data = photoData
        }
    }

    class FeedTimelineViewHolder(private val binding: ItemFeedTimelineTypeBinding) :
        FeedViewHolder(binding) {
        override fun onBind(data: FeedData) {
            val timelineData = data as FeedTimelineData
            binding.data = timelineData
            binding.ivFishPhoto.adapter = TimelineViewPagerAdapter(data.photoUrlList)
            binding.indicator.setViewPager2(binding.ivFishPhoto)
            binding.rvTimeline.adapter = TimelineRecyclerViewAdapter(data.timeline)
            binding.rvTimeline.visibility = View.GONE
            binding.ivShowAll.setImageResource(R.drawable.ic_baseline_arrow_drop_down_primary)
            binding.tvShowTimeline.setText(R.string.feed_show_timeline)
            setListener()
        }

        private fun setListener() {
            binding.timelineClickListener = {
                if (binding.rvTimeline.visibility == View.GONE) {
                    binding.rvTimeline.visibility = View.VISIBLE
                    binding.ivShowAll.setImageResource(R.drawable.ic_baseline_arrow_drop_up_primary)
                    binding.tvShowTimeline.setText(R.string.feed_close_timeline)
                } else {
                    binding.rvTimeline.visibility = View.GONE
                    binding.ivShowAll.setImageResource(R.drawable.ic_baseline_arrow_drop_down_primary)
                    binding.tvShowTimeline.setText(R.string.feed_show_timeline)
                }
            }
        }
    }

    companion object {
        const val PHOTO_TYPE = 0
        const val TIMELINE_TYPE = 1
        private val diffCallback = object : DiffUtil.ItemCallback<FeedData>() {
            override fun areItemsTheSame(oldItem: FeedData, newItem: FeedData): Boolean {
                Log.d("pagingAdapter", "Diff 실행: ${oldItem.date}, ${newItem.date}")
                return oldItem.date == newItem.date
            }


            override fun areContentsTheSame(oldItem: FeedData, newItem: FeedData): Boolean =
                oldItem == newItem
        }
    }
}

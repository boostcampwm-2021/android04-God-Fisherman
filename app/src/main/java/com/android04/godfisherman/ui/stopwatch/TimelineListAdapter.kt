package com.android04.godfisherman.ui.stopwatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.ItemTimeLineInStopWatchBinding

class TimelineListAdapter : ListAdapter<TimeLineDataTest, TimelineListAdapter.TimeLineViewHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        return TimeLineViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TimeLineViewHolder(private val binding: ItemTimeLineInStopWatchBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TimeLineDataTest) {
            binding.item = item
            binding.executePendingBindings()
        }

        companion object {
            fun create(parent: ViewGroup): TimeLineViewHolder =
                ItemTimeLineInStopWatchBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ).let { TimeLineViewHolder(it) }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<TimeLineDataTest>() {

        override fun areItemsTheSame(oldItem: TimeLineDataTest, newItem: TimeLineDataTest): Boolean {
            return newItem == oldItem
        }

        override fun areContentsTheSame(oldItem: TimeLineDataTest, newItem: TimeLineDataTest): Boolean {
            return newItem.time == oldItem.time && newItem.fishTag == oldItem.fishTag && newItem.length == oldItem.length
        }
    }
}

package com.android04.godfisherman.ui.stopwatch

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android04.godfisherman.databinding.ItemTimeLineInStopWatchBinding
import com.android04.godfisherman.data.localdatabase.entity.TmpFishingRecord

class TimelineListAdapter : ListAdapter<TmpFishingRecord, TimelineListAdapter.TimeLineViewHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        return TimeLineViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TimeLineViewHolder(private val binding: ItemTimeLineInStopWatchBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TmpFishingRecord) {
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

    class ItemComparator : DiffUtil.ItemCallback<TmpFishingRecord>() {

        override fun areItemsTheSame(oldItem: TmpFishingRecord, newItem: TmpFishingRecord): Boolean {
            return newItem.date == oldItem.date
        }

        override fun areContentsTheSame(oldItem: TmpFishingRecord, newItem: TmpFishingRecord): Boolean {
            return newItem == oldItem
        }
    }
}

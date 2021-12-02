package com.android04.godfisherman.ui.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android04.godfisherman.databinding.ItemTimelineBinding
import com.android04.godfisherman.presentation.feed.TimeLineData

class TimelineRecyclerViewAdapter(private val data: List<TimeLineData>) :
    RecyclerView.Adapter<TimelineRecyclerViewAdapter.TimelineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val binding = ItemTimelineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimelineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class TimelineViewHolder(private val binding: ItemTimelineBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: TimeLineData) {
            binding.data = data
            changeLine()
        }

        private fun changeLine() {
            when {
                isAlone() -> {
                    binding.ivLineDown.visibility = View.INVISIBLE
                    binding.ivLineUp.visibility = View.INVISIBLE
                }
                isFirst() -> {
                    binding.ivLineUp.visibility = View.INVISIBLE
                }
                isLast() -> {
                    binding.ivLineDown.visibility = View.INVISIBLE
                }
            }
        }

        private fun isFirst() = bindingAdapterPosition == 0

        private fun isLast() = bindingAdapterPosition == itemCount - 1

        private fun isAlone() = itemCount == 1 && bindingAdapterPosition == itemCount - 1
    }
}

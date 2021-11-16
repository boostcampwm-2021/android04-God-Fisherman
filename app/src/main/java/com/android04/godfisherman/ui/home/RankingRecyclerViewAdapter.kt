package com.android04.godfisherman.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android04.godfisherman.databinding.ItemHomeRankingBinding
import com.android04.godfisherman.databinding.ItemWaitingRankingBinding

class RankingRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data = mutableListOf<RankingData>()
    private var limitItemCount: Int? = null

    fun setData(newData : List<RankingData>) {
        data.addAll(newData)
        notifyDataSetChanged()
    }

    fun setLimitItemCount(limit: Int) {
        limitItemCount = limit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_SIZE) {
            val binding = ItemHomeRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return RankingViewHolder(binding)
        } else {
            val binding = ItemWaitingRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return WaitingRankingViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RankingViewHolder) {
            holder.onBind(data[position] as RankingData.HomeRankingData)
        } else {
            (holder as WaitingRankingViewHolder).onBind(data[position] as RankingData.HomeWaitingRankingData)
        }
    }

    override fun getItemCount(): Int {
        if (limitItemCount == null) return data.size
        return if (data.size < limitItemCount!!) data.size else limitItemCount!!
    }

    override fun getItemViewType(position: Int): Int =
        when(data[position]) {
            is RankingData.HomeRankingData -> TYPE_SIZE
            is RankingData.HomeWaitingRankingData -> TYPE_WAITING
        }

    inner class RankingViewHolder(private val binding : ItemHomeRankingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data : RankingData.HomeRankingData) {
            binding.data = data
            binding.rank = (adapterPosition + 1).toString() + "위"
            setLastView()
        }

        private fun setLastView() {
            if (adapterPosition == itemCount - 1) {
                binding.vLine.visibility = View.INVISIBLE
            } else {
                binding.vLine.visibility = View.VISIBLE
            }
        }
    }

    inner class WaitingRankingViewHolder(private val binding : ItemWaitingRankingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data : RankingData.HomeWaitingRankingData) {
            binding.data = data
            binding.rank = (adapterPosition + 1).toString() + "위"
            setLastView()
        }

        private fun setLastView() {
            if (adapterPosition == itemCount - 1) {
                binding.vLine.visibility = View.INVISIBLE
            } else {
                binding.vLine.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        const val TYPE_SIZE = 1
        const val TYPE_WAITING = 2
    }
}

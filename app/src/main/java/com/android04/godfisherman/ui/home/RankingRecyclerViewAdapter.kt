package com.android04.godfisherman.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android04.godfisherman.databinding.ItemHomeRankingBinding

class RankingRecyclerViewAdapter : RecyclerView.Adapter<RankingRecyclerViewAdapter.RankingViewHolder>() {

    private val data = mutableListOf<HomeRankingData>()

    fun setData(newData : List<HomeRankingData>) {
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val binding = ItemHomeRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RankingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RankingViewHolder(private val binding : ItemHomeRankingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data : HomeRankingData) {
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
}
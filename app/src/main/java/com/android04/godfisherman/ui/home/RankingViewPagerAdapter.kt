package com.android04.godfisherman.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android04.godfisherman.databinding.ItemRankingBinding

class RankingViewPagerAdapter : RecyclerView.Adapter<RankingViewPagerAdapter.RankingPageViewHolder>() {

    private val data = mutableListOf<List<RankingData>>()

    fun setData(newData : List<List<RankingData>>) {
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingPageViewHolder {
        val binding = ItemRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RankingPageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankingPageViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int  = data.size

    class RankingPageViewHolder(private val binding: ItemRankingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(rankingList: List<RankingData>) {
            binding.rvRanking.adapter = RankingRecyclerViewAdapter()
            (binding.rvRanking.adapter as RankingRecyclerViewAdapter).setData(rankingList)
        }
    }
}

package com.android04.godfisherman.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android04.godfisherman.databinding.ItemHomeRecommendBinding

class RecommendRecyclerViewAdapter : RecyclerView.Adapter<RecommendRecyclerViewAdapter.RecommendViewHolder>() {

    private val data = mutableListOf<HomeRecommendData>()

    fun setData(newData : List<HomeRecommendData>) {
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendViewHolder {
        val binding = ItemHomeRecommendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class RecommendViewHolder(private val binding : ItemHomeRecommendBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data : HomeRecommendData) {
            binding.data = data
        }

    }
}
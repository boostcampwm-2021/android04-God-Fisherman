package com.android04.godfisherman.ui.home

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android04.godfisherman.databinding.ItemHomeRecommendBinding
import com.android04.godfisherman.presentation.home.HomeRecommendData

class RecommendRecyclerViewAdapter : RecyclerView.Adapter<RecommendRecyclerViewAdapter.RecommendViewHolder>() {

    private val data = mutableListOf<HomeRecommendData>()

    fun setData(newData: List<HomeRecommendData>) {
        data.clear()
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

    inner class RecommendViewHolder(private val binding: ItemHomeRecommendBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: HomeRecommendData) {
            binding.data = data
            binding.root.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(data.videoUrl)
                intent.setPackage("com.google.android.youtube")

                binding.root.context.startActivity(intent)
            }
        }

    }
}

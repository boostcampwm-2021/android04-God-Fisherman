package com.android04.godfisherman.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android04.godfisherman.databinding.ItemTimelinePhotoBinding

class TimelineViewPagerAdapter(private val data: List<String>) : RecyclerView.Adapter<TimelineViewPagerAdapter.PhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemTimelinePhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class PhotoViewHolder(private val binding: ItemTimelinePhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(url: String) {
            binding.url = url
        }
    }
}

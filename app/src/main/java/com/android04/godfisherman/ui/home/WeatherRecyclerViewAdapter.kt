package com.android04.godfisherman.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android04.godfisherman.databinding.ItemWeatherDetailBinding
import com.android04.godfisherman.presentation.home.HomeDetailWeather

class WeatherRecyclerViewAdapter : RecyclerView.Adapter<WeatherRecyclerViewAdapter.WeatherViewHolder>() {

    private val data = mutableListOf<HomeDetailWeather>()

    fun setData(new: List<HomeDetailWeather>) {
        data.clear()
        data.addAll(new)
        notifyDataSetChanged()
    }

    class WeatherViewHolder(private val binding: ItemWeatherDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: HomeDetailWeather) {
            binding.data = data
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ItemWeatherDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.count()
    }
}
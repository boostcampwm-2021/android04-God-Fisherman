package com.android04.godfisherman.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.common.App
import com.android04.godfisherman.common.EventObserver
import com.android04.godfisherman.databinding.FragmentHomeBinding
import com.android04.godfisherman.ui.base.BaseFragment
import com.android04.godfisherman.ui.main.MainActivity
import com.android04.godfisherman.utils.showToast
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home) {

    override val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        setStatusBarColor(R.color.background_home)
        setLocationSavedListener()
        loadLocation()
        initView()
        setListener()
        setRecyclerView()
        setupObserver()

        viewModel.fetchUserID()
        viewModel.fetchYoutube()
        viewModel.fetchRanking()

    }

    private fun setLocationSavedListener() {
        parentFragmentManager.setFragmentResultListener(
            LOCATION_UPDATED,
            viewLifecycleOwner
        ) { key, bundle ->
            val isSaved = bundle.getBoolean(MainActivity.DEFAULT_BUNDLE)
            Log.d("LocationUpdate", "통신 완료 : $isSaved")
            if (isSaved) viewModel.loadLocation()
        }
    }

    private fun initView() {
        binding.tvUserName.isSelected = true
    }

    private fun setRecyclerView() {
        binding.rvRanking.adapter = RankingRecyclerViewAdapter()
        (binding.rvRanking.adapter as RankingRecyclerViewAdapter).setLimitItemCount(5)

        binding.rvRecommend.adapter = RecommendRecyclerViewAdapter()
        binding.rvWeatherDetail.adapter = WeatherRecyclerViewAdapter()
    }

    private fun setupObserver() {
        viewModel.youtubeList.observe(viewLifecycleOwner) {
            (binding.rvRecommend.adapter as RecommendRecyclerViewAdapter).setData(it)
        }

        viewModel.rankList.observe(viewLifecycleOwner) {
            (binding.rvRanking.adapter as RankingRecyclerViewAdapter).setData(it)
        }

        viewModel.address.observe(viewLifecycleOwner) {
            viewModel.fetchWeather()
        }

        viewModel.homeDetailWeather.observe(viewLifecycleOwner) {
            (binding.rvWeatherDetail.adapter as WeatherRecyclerViewAdapter).setData(it)
        }

        viewModel.error.observe(viewLifecycleOwner, EventObserver { message ->
            showToast(requireContext(), message)
        })
    }

    private fun setListener() {
        binding.detailClickListener = {
            if (binding.rvWeatherDetail.visibility == View.VISIBLE) {
                binding.rvWeatherDetail.visibility = View.GONE
                binding.ivShowAll.setImageResource(R.drawable.ic_baseline_arrow_drop_down_primary)
                binding.tvShowWeather.setText(R.string.home_show_weather)

            } else {
                binding.rvWeatherDetail.visibility = View.VISIBLE
                binding.ivShowAll.setImageResource(R.drawable.ic_baseline_arrow_drop_up_primary)
                binding.tvShowWeather.setText(R.string.home_close_weather)
            }
        }
        binding.tvShowAll.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fl_fragment_container, RankingDetailFragment())
                addToBackStack("home")
                commit()
            }
        }
        binding.srlHome.setOnRefreshListener {
            viewModel.fetchYoutube(true)
            viewModel.fetchRanking(true)
            viewModel.fetchWeather()
            binding.srlHome.isRefreshing = false
        }
    }

    override fun onStart() {
        super.onStart()
        val application = requireActivity().application as App
        if (application.exitCameraActivityFlag) {
            requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).menu.apply {
                findItem(R.id.navigation_camera).isChecked = false
                findItem(R.id.navigation_home).isChecked = true
            }
        }
        application.exitCameraActivityFlag = false
    }

    private fun loadLocation() {
        viewModel.loadLocation()
    }

    companion object {
        const val LOCATION_UPDATED = "locationUpdated"
    }
}

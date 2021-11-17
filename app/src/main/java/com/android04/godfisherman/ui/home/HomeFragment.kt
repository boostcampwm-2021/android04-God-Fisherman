package com.android04.godfisherman.ui.home

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.common.App
import com.android04.godfisherman.databinding.FragmentHomeBinding
import com.android04.godfisherman.network.RetrofitClient
import com.android04.godfisherman.network.response.CurrentWeatherResponse
import retrofit2.Callback
import com.android04.godfisherman.network.response.WeatherResponse
import com.android04.godfisherman.ui.base.BaseFragment
import com.android04.godfisherman.utils.isGrantedLocationPermission
import com.android04.godfisherman.utils.showToast
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home) {

    override val viewModel: HomeViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        setListener()
        setRecyclerView()
        setObserver()
        updateLocation()

        viewModel.fetchYoutube()
    }

    private fun setRecyclerView() {
        binding.rvRanking.adapter = RankingRecyclerViewAdapter()
        (binding.rvRanking.adapter as RankingRecyclerViewAdapter).setData(
            listOf(
                HomeRankingData(
                    "아이비", "물개", 140.5
                ),
                HomeRankingData(
                    "아이비", "물개", 140.5
                ),
                HomeRankingData(
                    "아이비", "물개", 140.5
                ),
                HomeRankingData(
                    "아이비", "물개", 140.5
                ),
                HomeRankingData(
                    "아이비", "물개", 140.5
                )
            )
        )

        binding.rvRecommend.adapter = RecommendRecyclerViewAdapter()
        binding.rvWeatherDetail.adapter = WeatherRecyclerViewAdapter()
    }

    private fun setObserver() {
        viewModel.youtubeList.observe(viewLifecycleOwner) {
            (binding.rvRecommend.adapter as RecommendRecyclerViewAdapter).setData(it)
        }
        viewModel.isYoutubeLoading.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.lottieLoading.visibility = View.VISIBLE
            } else {
                binding.lottieLoading.visibility = View.INVISIBLE
            }
        }
        viewModel.isYoutubeSuccess.observe(viewLifecycleOwner) {
            if (!it) {
                showToast(requireContext(), R.string.home_recommend_fail)
            }
        }
        viewModel.currentLocation.observe(viewLifecycleOwner) {
            viewModel.fetchWeather()
        }
        viewModel.homeDetailWeather.observe(viewLifecycleOwner) {
            (binding.rvWeatherDetail.adapter as WeatherRecyclerViewAdapter).setData(it)
        }
        viewModel.isWeatherLoading.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.lottieWeatherLoading.visibility = View.VISIBLE
                binding.tvSunriseDesc.visibility = View.INVISIBLE
                binding.tvSunsetDesc.visibility = View.INVISIBLE
                binding.ivWeatherIcon.visibility = View.INVISIBLE
                binding.layoutShowAll.isEnabled = false
            } else {
                binding.lottieWeatherLoading.visibility = View.INVISIBLE
                binding.tvSunriseDesc.visibility = View.VISIBLE
                binding.tvSunsetDesc.visibility = View.VISIBLE
                binding.ivWeatherIcon.visibility = View.VISIBLE
                binding.layoutShowAll.isEnabled = true
            }
        }
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
    }

    private fun updateLocation() {
        if (isGrantedLocationPermission(requireContext())) {
            viewModel.updateLocation()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
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

    companion object {
        const val REQUEST_CODE_LOCATION = 2
    }
}

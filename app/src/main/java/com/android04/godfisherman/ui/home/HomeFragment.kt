package com.android04.godfisherman.ui.home

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.common.App
import com.android04.godfisherman.databinding.FragmentHomeBinding
import com.android04.godfisherman.ui.base.BaseFragment
import com.android04.godfisherman.utils.isGrantedLocationPermission
import com.android04.godfisherman.utils.showToast
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home) {

    override val viewModel: HomeViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        setRecyclerView()
        setObserver()
        updateLocation()
        viewModel.fetchYoutube()
        viewModel.fetchRanking()
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
    }

    private fun setObserver() {
        viewModel.youtubeList.observe(viewLifecycleOwner) {
            (binding.rvRecommend.adapter as RecommendRecyclerViewAdapter).setData(it)
        }
        viewModel.isYoutubeSuccess.observe(viewLifecycleOwner) {
            if (!it) {
                showToast(requireContext(), R.string.home_recommend_fail)
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

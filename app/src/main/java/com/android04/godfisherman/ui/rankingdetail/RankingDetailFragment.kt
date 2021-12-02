package com.android04.godfisherman.ui.rankingdetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.FragmentRankingDetailBinding
import com.android04.godfisherman.presentation.rankingdetail.RankingDetailViewModel
import com.android04.godfisherman.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RankingDetailFragment : BaseFragment<FragmentRankingDetailBinding, RankingDetailViewModel>(R.layout.fragment_ranking_detail) {

    override val viewModel: RankingDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()
        setupObserver()
        viewModel.fetchRanking()
    }

    private fun setupObserver() {
        viewModel.rankList.observe(viewLifecycleOwner) {
            binding.lottieLoading.visibility = View.GONE
            binding.lottieLoading.pauseAnimation()
            binding.tvRankingHelp.visibility = View.GONE
            binding.indicatorRanking.visibility = View.VISIBLE
            (binding.vpRanking.adapter as RankingViewPagerAdapter).setData(it)
        }
    }

    private fun initViewPager() {
        binding.vpRanking.adapter = RankingViewPagerAdapter()
        binding.vpRanking.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.indicatorRanking.setViewPager2(binding.vpRanking)
    }
}

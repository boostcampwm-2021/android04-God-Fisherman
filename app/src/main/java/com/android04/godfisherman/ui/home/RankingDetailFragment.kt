package com.android04.godfisherman.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.FragmentRankingDetailBinding
import com.android04.godfisherman.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RankingDetailFragment : BaseFragment<FragmentRankingDetailBinding, RankingDetailViewModel>(R.layout.fragment_ranking_detail) {

    override val viewModel: RankingDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewPager()
        setObserver()
        viewModel.fetchRanking()
    }


    private fun setObserver() {
        viewModel.rankList.observe(viewLifecycleOwner) {
            (binding.vpRanking.adapter as RankingViewPagerAdapter).setData(it)
        }
    }

    private fun setViewPager() {
        binding.vpRanking.adapter = RankingViewPagerAdapter()
        binding.vpRanking.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }
}

package com.android04.godfisherman.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.FragmentRankingDetailBinding
import com.android04.godfisherman.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RankingDetailFragment : BaseFragment<FragmentRankingDetailBinding, RankingDetailViewModel>(R.layout.fragment_ranking_detail) {

    override val viewModel: RankingDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        setObserver()
        viewModel.fetchRanking()
    }

    private fun setObserver() {
        viewModel.rankList.observe(viewLifecycleOwner) {
            Log.d("TAG", "setObserver: $it")
            (binding.rvRanking.adapter as RankingRecyclerViewAdapter).setData(it)
        }
    }


    private fun setRecyclerView() {
        binding.rvRanking.adapter = RankingRecyclerViewAdapter()
        viewModel.rankList.value?.let {
        }
    }
}

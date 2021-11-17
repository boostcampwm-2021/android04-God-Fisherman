package com.android04.godfisherman.ui.feed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.common.Type
import com.android04.godfisherman.databinding.FragmentFeedBinding
import com.android04.godfisherman.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : BaseFragment<FragmentFeedBinding, FeedViewModel>(R.layout.fragment_feed) {
    override val viewModel: FeedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFeed.adapter = FeedRecyclerViewAdapter()
        binding.feedViewModel = viewModel

        setRefresh()
        initListener()
        setupObserver()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchFeedDataList(Type.ALL)
    }

    private fun setRefresh(){
        binding.SRLFeed.setOnRefreshListener {
            (binding.rvFeed.adapter as FeedRecyclerViewAdapter).clearData()
            viewModel.setFilter(binding.cgType.checkedChipId)

        }
    }

    private fun initListener() {
        binding.cgType.setOnCheckedChangeListener { _, checkedId ->
            viewModel.setFilter(checkedId)
            (binding.rvFeed.adapter as FeedRecyclerViewAdapter).clearData()

            binding.lottieLoading.visibility = View.VISIBLE
            binding.lottieLoading.playAnimation()

            binding.cgType.visibility = View.INVISIBLE
        }
    }

    private fun setupObserver() {
        viewModel.feedDataList.observe(viewLifecycleOwner) {
            binding.lottieLoading.visibility = View.GONE
            binding.lottieLoading.pauseAnimation()

            binding.cgType.visibility = View.VISIBLE
            binding.SRLFeed.isRefreshing = false
            (binding.rvFeed.adapter as FeedRecyclerViewAdapter).setData(it)
        }
    }
}

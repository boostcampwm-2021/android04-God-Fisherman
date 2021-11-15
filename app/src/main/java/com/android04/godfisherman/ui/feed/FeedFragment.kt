package com.android04.godfisherman.ui.feed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android04.godfisherman.R
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

        initListener()
        setupObserver()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchFeedDataList()
    }

    private fun initListener() {
        binding.cgType.setOnCheckedChangeListener { group, checkedId ->
            viewModel.setFilter(checkedId)
        }
    }

    private fun setupObserver() {
        viewModel.feedDataList.observe(viewLifecycleOwner) {
            (binding.rvFeed.adapter as FeedRecyclerViewAdapter).setData(it)
        }
    }
}

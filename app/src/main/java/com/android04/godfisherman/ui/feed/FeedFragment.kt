package com.android04.godfisherman.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.FragmentFeedBinding
import com.android04.godfisherman.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedFragment : BaseFragment<FragmentFeedBinding, FeedViewModel>(R.layout.fragment_feed) {
    override val viewModel: FeedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.feedFragment = this
        binding.rvFeed.adapter = FeedAdapter()
        binding.feedViewModel = viewModel
        setStatusBarColor(R.color.basic)
        initListener()
        initRecyclerView()
    }

    private fun initListener() {
        binding.cgType.setOnCheckedChangeListener { _, checkedId ->
            initRecyclerView()
        }
        binding.SRLFeed.setOnRefreshListener {
            initRecyclerView()
        }
    }

    private fun initRecyclerView() {
        binding.rvFeed.adapter = FeedAdapter()
        pagingStateListener()
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            viewModel.setFilter(binding.cgType.checkedChipId).collectLatest { newData ->
                (binding.rvFeed.adapter as FeedAdapter).submitData(newData)
            }
        }
    }

    private fun pagingStateListener() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            (binding.rvFeed.adapter as FeedAdapter).loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        viewModel.setLoadingOn()
                    }
                    !is LoadState.Loading -> {
                        viewModel.setLoadingOff()
                    }
                    is LoadState.Error -> {
                        Log.d("LoadState", "에러 발생")
                    }
                }
            }
        }
    }

}

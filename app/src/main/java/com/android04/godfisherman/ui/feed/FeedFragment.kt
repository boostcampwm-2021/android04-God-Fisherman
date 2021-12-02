package com.android04.godfisherman.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.FragmentFeedBinding
import com.android04.godfisherman.presentation.feed.FeedViewModel
import com.android04.godfisherman.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Exception

@AndroidEntryPoint
class FeedFragment : BaseFragment<FragmentFeedBinding, FeedViewModel>(R.layout.fragment_feed) {
    override val viewModel: FeedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.basic)
        initBinding()
        initListener()
        initRecyclerView()
    }

    private fun initBinding() {
        binding.feedFragment = this
        binding.feedViewModel = viewModel
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
        initPagingStateListener()
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            viewModel.setFilter(binding.cgType.checkedChipId).collectLatest { newData ->
                (binding.rvFeed.adapter as FeedAdapter).submitData(newData)
            }
        }
    }

    private fun initPagingStateListener() {
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
                        throw Exception("페이징 로딩 에러")
                    }
                }
            }
        }
    }

}

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
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class FeedFragment : BaseFragment<FragmentFeedBinding, FeedViewModel>(R.layout.fragment_feed) {
    override val viewModel: FeedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFeed.adapter = FeedAdapter()
        binding.feedViewModel = viewModel
        setStatusBarColor(R.color.basic)

        setRefresh()
        initListener()
        initRecyclerView()
    }

    private fun setRefresh(){
        binding.SRLFeed.setOnRefreshListener {
            binding.rvFeed.adapter = FeedAdapter()
            pagingStateListener()
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                viewModel.setFilter(binding.cgType.checkedChipId).collectLatest { newData ->
                    Log.d("paging3", "collect : $newData")
                    (binding.rvFeed.adapter as FeedAdapter).submitData(newData)
                }
            }
        }
    }

    private fun initListener() {
        binding.cgType.setOnCheckedChangeListener { _, checkedId ->
            binding.rvFeed.adapter = FeedAdapter()
            pagingStateListener()
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                viewModel.setFilter(checkedId).collectLatest { newData ->
                    Log.d("paging3", "collect : $newData")
                    (binding.rvFeed.adapter as FeedAdapter).submitData(newData)
                }
            }
        }
    }

    private fun initRecyclerView(){
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            binding.rvFeed.adapter = FeedAdapter()
            pagingStateListener()
            viewModel.setFilter(binding.cgType.checkedChipId).collectLatest { newData ->
                (binding.rvFeed.adapter as FeedAdapter).submitData(newData)
            }
        }
    }

    private fun pagingStateListener(){
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            (binding.rvFeed.adapter as FeedAdapter).loadStateFlow.collectLatest { loadStates ->
                when(loadStates.refresh){
                    is LoadState.Loading -> {
                        Log.d("LoadState", "로딩 중")
                        withContext(Dispatchers.Main){
                            binding.lottieLoading.visibility = View.VISIBLE
                            binding.lottieLoading.playAnimation()
                            binding.cpTypeAll.isEnabled = false
                            binding.cpTypePhoto.isEnabled = false
                            binding.cpTypeTimeline.isEnabled = false
                        }
                    }
                    !is LoadState.Loading -> {
                        Log.d("LoadState", "로딩 끝")
                        withContext(Dispatchers.Main){
                            binding.lottieLoading.pauseAnimation()
                            binding.lottieLoading.visibility = View.GONE
                            binding.SRLFeed.isRefreshing = false
                            binding.cpTypeAll.isEnabled = true
                            binding.cpTypePhoto.isEnabled = true
                            binding.cpTypeTimeline.isEnabled = true
                        }
                    }
                    is LoadState.Error -> {
                        Log.d("LoadState", "에러 발생")
                    }
                }
            }
        }

    }
}

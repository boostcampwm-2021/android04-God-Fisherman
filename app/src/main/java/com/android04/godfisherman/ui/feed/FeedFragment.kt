package com.android04.godfisherman.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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

        binding.rvFeed.adapter = FeedAdapter()
        binding.feedViewModel = viewModel
        setStatusBarColor(R.color.basic)
        
        setupObserver()
        setRefresh()
        initListener()
        initRecyclerView()
    }

    private fun setRefresh(){
        binding.SRLFeed.setOnRefreshListener {
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
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                viewModel.setFilter(checkedId).collectLatest { newData ->
                    Log.d("paging3", "collect : $newData")
                    (binding.rvFeed.adapter as FeedAdapter).submitData(newData)
                }
            }
        }
    }

    private fun setupObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            Log.d("isLoading3", "$it")
            if (it) {
                binding.lottieLoading.visibility = View.VISIBLE
                binding.lottieLoading.playAnimation()
                binding.cgType.visibility = View.INVISIBLE
            } else {
                binding.lottieLoading.visibility = View.GONE
                binding.lottieLoading.pauseAnimation()
                binding.cgType.visibility = View.VISIBLE
                binding.SRLFeed.isRefreshing = false
            }
        }
    }

    private fun initRecyclerView(){
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            binding.rvFeed.adapter = FeedAdapter()
            viewModel.setFilter(binding.cgType.checkedChipId).collectLatest { newData ->
                (binding.rvFeed.adapter as FeedAdapter).submitData(newData)
            }
        }
    }
}

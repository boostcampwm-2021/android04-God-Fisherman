package com.android04.godfisherman.ui.stopwatch

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.FragmentStopwatchBinding
import com.android04.godfisherman.ui.base.BaseFragment
import com.android04.godfisherman.ui.main.MainActivity
import com.android04.godfisherman.ui.main.MainViewModel
import com.android04.godfisherman.utils.UploadDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestStopwatchFragment :
    BaseFragment<FragmentStopwatchBinding, MainViewModel>(R.layout.fragment_stopwatch) {

    override val viewModel: MainViewModel by activityViewModels()

    private var isPlayAnimate = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        initRecyclerView()
        setObserver()

        binding.viewStartStop.setOnClickListener {
            if (viewModel.startOrStopTimer()) {
                showDialog()
                viewModel.resetStopwatch()
            }
        }

        binding.nsvStopwatch.setOnScrollChangeListener { _: NestedScrollView, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY) {
                Log.i("TAG", "Scroll DOWN")
                (requireActivity() as MainActivity).setMotionSwipeAreaVisibility(View.GONE)
            }
            if (scrollY == 0) {
                Log.i("TAG", "TOP SCROLL")
                (requireActivity() as MainActivity).setMotionSwipeAreaVisibility(View.VISIBLE)
            }
        }
    }

    private fun showDialog() {
        val dialog = UploadDialog(requireContext())
        dialog.setUploadOnClickListener(object : UploadDialog.OnDialogClickListener {
            override fun onClicked() {
                Log.d("UploadDialog", "upload")
                viewModel.saveTimeLineRecord()
            }
        })
        dialog.setBackOnClickListener(object : UploadDialog.OnDialogClickListener {
            override fun onClicked() {
                Log.d("UploadDialog", "back")
                viewModel.resumeStopwatch()
            }
        })
        dialog.showDialog()
    }

    private fun initRecyclerView() {
        viewModel.loadTmpTimeLineRecord()
        val recyclerViewEmptySupport = binding.rvTimeLine
        val emptyView = binding.tvEmptyView
        recyclerViewEmptySupport.adapter = TimelineListAdapter()
        recyclerViewEmptySupport.setEmptyView(emptyView)
        recyclerViewEmptySupport.setVerticalInterval(50)
    }

    private fun setObserver() {
        viewModel.isStopwatchStarted.observe(viewLifecycleOwner, Observer {
            binding.vShadow.isVisible = it
            isPlayAnimate = it
            if (it) {
                lifecycleScope.launchWhenStarted {
                    animateShadow()
                }
            }
        })
        viewModel.tmpFishingList.observe(viewLifecycleOwner, Observer {
            binding.rvTimeLine.submitList(it)
        })
    }

    private fun animateShadow() {
        if (isPlayAnimate) {
            Log.d("animateShadow", "메소드 실행")
            binding.vShadow.apply {
                animate().scaleX(1.1f).scaleY(1.1f).setDuration(1000).withEndAction {
                    animate().scaleX(1f).scaleY(1f).setDuration(1000).withEndAction {
                        animateShadow()
                    }.start()
                }.start()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        isPlayAnimate = false
    }

}

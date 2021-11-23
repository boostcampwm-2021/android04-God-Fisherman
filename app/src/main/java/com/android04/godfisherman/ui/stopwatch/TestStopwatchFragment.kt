package com.android04.godfisherman.ui.stopwatch

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.android04.godfisherman.R
import com.android04.godfisherman.common.EventObserver
import com.android04.godfisherman.common.LoadingDialogProvider
import com.android04.godfisherman.databinding.FragmentStopwatchBinding
import com.android04.godfisherman.ui.base.BaseFragment
import com.android04.godfisherman.ui.main.MainActivity
import com.android04.godfisherman.ui.main.MainViewModel
import com.android04.godfisherman.utils.UploadDialog
import com.android04.godfisherman.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestStopwatchFragment :
    BaseFragment<FragmentStopwatchBinding, MainViewModel>(R.layout.fragment_stopwatch) {

    override val viewModel: MainViewModel by activityViewModels()

    private var isPlayAnimate = false
    private val loadingDialog: Dialog by lazy {
        LoadingDialogProvider().provideLoadingDialog(requireContext(), R.layout.dialog_upload_loading)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.fragment = this

        initRecyclerView()
        setupListener()
        setupObserver()
    }

    fun showDialog() {
        val dialog = UploadDialog(requireContext())
        dialog.setUploadOnClickListener(object : UploadDialog.OnDialogClickListener {
            override fun onClicked() {
                viewModel.saveTimeLineRecord()
            }
        })
        dialog.setBackOnClickListener(object : UploadDialog.OnDialogClickListener {
            override fun onClicked() {
                viewModel.resumeStopwatch()
            }
        })
        dialog.showDialog()
    }

    private fun initRecyclerView() {
        viewModel.loadTmpTimeLineRecord()
        binding.rvTimeLine.apply {
            setUpConfiguration(
                TimelineListAdapter(),
                binding.tvEmptyView,
                50
            )
        }
    }

    private fun setupObserver() {
        viewModel.isStopwatchStarted.observe(viewLifecycleOwner, {
            binding.vShadow.isVisible = it
            isPlayAnimate = it
            if (it) {
                lifecycleScope.launchWhenStarted {
                    animateShadow()
                }
            }
        })
        viewModel.isLoading.observe(viewLifecycleOwner) {
            when (it) {
                true -> loadingDialog.show()
                false -> loadingDialog.cancel()
            }
        }
        viewModel.successOrFail.observe(viewLifecycleOwner, EventObserver { message ->
            showToast(requireContext(), message)
        })
    }

    private fun setupListener() {

        binding.nsvStopwatch.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY) {
                (requireActivity() as MainActivity).setMotionSwipeAreaVisibility(View.GONE)
            }
            if (scrollY == 0) {
                (requireActivity() as MainActivity).setMotionSwipeAreaVisibility(View.VISIBLE)
            }
        }
    }

    private fun animateShadow() {
        if (isPlayAnimate) {
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

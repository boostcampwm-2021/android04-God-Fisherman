package com.android04.godfisherman.ui.stopwatch

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.FragmentStopwatchInfoBinding
import com.android04.godfisherman.ui.base.BaseFragment
import com.android04.godfisherman.ui.main.MainActivity

class StopwatchInfoFragment : BaseFragment<FragmentStopwatchInfoBinding, StopwatchInfoViewModel>(R.layout.fragment_stopwatch_info) {
    override val viewModel: StopwatchInfoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStatusBarColor(R.color.gradient_main_start)
        binding.btnStart.setOnClickListener {
            val activity = requireActivity() as MainActivity
            activity.viewModel.stopwatchOnFlag.value = true
//            activity.viewModel.isFromInfoFragment = true
            activity.removeFragment()
        }
    }
}

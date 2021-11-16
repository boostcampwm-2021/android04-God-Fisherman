package com.android04.godfisherman.ui.stopwatch

import androidx.fragment.app.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.FragmentTestStopwatchBinding
import com.android04.godfisherman.ui.base.BaseFragment

class TestStopwatchFragment : BaseFragment<FragmentTestStopwatchBinding, StopwatchInfoViewModel>(R.layout.fragment_test_stopwatch) {

    override val viewModel: StopwatchInfoViewModel by viewModels()

}

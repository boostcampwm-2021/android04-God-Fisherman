package com.android04.godfisherman.ui.stopwatch

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.FragmentStopwatchInfoBinding
import com.android04.godfisherman.ui.base.BaseFragment

class StopwatchInfoFragment : BaseFragment<FragmentStopwatchInfoBinding, StopwatchInfoViewModel>(R.layout.fragment_stopwatch_info) {
    override val viewModel: StopwatchInfoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val intent = Intent(requireContext(), StopwatchActivity::class.java)
        binding.btnStart.setOnClickListener { startActivity(intent) }
    }
}

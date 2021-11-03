package com.android04.godfisherman.ui.dashboard

import androidx.fragment.app.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.FragmentDashboardBinding
import com.android04.godfisherman.ui.base.BaseFragment

class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>(R.layout.fragment_dashboard) {
    override val viewModel: DashboardViewModel by viewModels()
}

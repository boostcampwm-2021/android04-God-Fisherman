package com.android04.godfisherman.ui.notifications

import androidx.fragment.app.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.FragmentNotificationsBinding
import com.android04.godfisherman.ui.base.BaseFragment

class NotificationsFragment : BaseFragment<FragmentNotificationsBinding, NotificationsViewModel>(R.layout.fragment_notifications) {
    override val viewModel: NotificationsViewModel by viewModels()
}

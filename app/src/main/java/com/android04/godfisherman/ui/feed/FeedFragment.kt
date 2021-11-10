package com.android04.godfisherman.ui.feed

import androidx.fragment.app.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.FragmentFeedBinding
import com.android04.godfisherman.ui.base.BaseFragment

class FeedFragment : BaseFragment<FragmentFeedBinding, FeedViewModel>(R.layout.fragment_feed) {
    override val viewModel: FeedViewModel by viewModels()
}

package com.android04.godfisherman.ui.home

import androidx.fragment.app.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.FragmentRankingDetailBinding
import com.android04.godfisherman.ui.base.BaseFragment


class RankingDetailFragment : BaseFragment<FragmentRankingDetailBinding, HomeViewModel>(R.layout.fragment_ranking_detail) {

    override val viewModel: HomeViewModel by viewModels()

}

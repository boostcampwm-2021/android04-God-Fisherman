package com.android04.godfisherman.ui.feed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.FragmentFeedBinding
import com.android04.godfisherman.ui.base.BaseFragment

class FeedFragment : BaseFragment<FragmentFeedBinding, FeedViewModel>(R.layout.fragment_feed) {
    override val viewModel: FeedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFeed.adapter = FeedRecyclerViewAdapter()
        (binding.rvFeed.adapter as FeedRecyclerViewAdapter).setData(
            listOf(
                FeedPhotoData(
                    "Test Name1",
                    "Test Location1",
                    "Test Date1",
                    "https://firebasestorage.googleapis.com/v0/b/god-fisherman.appspot.com/o/images%2FIMAGE_20211110_113728.jpg?alt=media&token=a266df02-cd13-4d9b-abe4-daf040e14df4",
                    "Test Type1",
                    130.9
                ),
                FeedTimelineData(
                    "Test Name2",
                    "Test Location2",
                    "Test Data2",
                    listOf("https://firebasestorage.googleapis.com/v0/b/god-fisherman.appspot.com/o/images%2FIMAGE_20211110_113728.jpg?alt=media&token=a266df02-cd13-4d9b-abe4-daf040e14df4", "https://firebasestorage.googleapis.com/v0/b/god-fisherman.appspot.com/o/images%2FIMAGE_20211110_222819.jpg?alt=media&token=5ff06326-e49d-47d0-92eb-49cc8e08a035", "https://firebasestorage.googleapis.com/v0/b/god-fisherman.appspot.com/o/images%2FIMAGE_20211110_222802.jpg?alt=media&token=04a9765b-b6bd-4409-b892-751a32e06db0"),
                    listOf(TimeLineData("Sample1", 123.0, "10:00"),
                        TimeLineData("Sample2", 123.0, "11:00"),
                        TimeLineData("Sample3", 123.0, "12:00"))
                ),
                FeedPhotoData(
                    "Test Name3",
                    "Test Location3",
                    "Test Date3",
                    "https://firebasestorage.googleapis.com/v0/b/god-fisherman.appspot.com/o/images%2FIMAGE_20211110_113728.jpg?alt=media&token=a266df02-cd13-4d9b-abe4-daf040e14df4",
                    "Test Type3",
                    130.9
                ),
                FeedTimelineData(
                    "Test Name4",
                    "Test Location4",
                    "Test Data4",
                    listOf("https://firebasestorage.googleapis.com/v0/b/god-fisherman.appspot.com/o/images%2FIMAGE_20211110_113728.jpg?alt=media&token=a266df02-cd13-4d9b-abe4-daf040e14df4"),
                    listOf(TimeLineData("Sample1", 123.0, "10:00"))
                ),
                FeedTimelineData(
                    "Test Name5",
                    "Test Location5",
                    "Test Data5",
                    listOf("https://firebasestorage.googleapis.com/v0/b/god-fisherman.appspot.com/o/images%2FIMAGE_20211110_113728.jpg?alt=media&token=a266df02-cd13-4d9b-abe4-daf040e14df4", "https://firebasestorage.googleapis.com/v0/b/god-fisherman.appspot.com/o/images%2FIMAGE_20211110_113728.jpg?alt=media&token=a266df02-cd13-4d9b-abe4-daf040e14df4"),
                    listOf(TimeLineData("Sample1", 123.0, "10:00"), TimeLineData("Sample1", 123.0, "11:00"))
                )
            )
        )
    }
}

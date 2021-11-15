package com.android04.godfisherman.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.ActivityMainBinding
import com.android04.godfisherman.ui.base.BaseActivity
import com.android04.godfisherman.ui.camera.CameraActivity
import com.android04.godfisherman.ui.feed.FeedFragment
import com.android04.godfisherman.ui.home.HomeFragment
import com.android04.godfisherman.ui.mypage.MyPageFragment
import com.android04.godfisherman.ui.stopwatch.StopwatchInfoFragment
import com.android04.godfisherman.ui.stopwatch.TestStopwatchFragment
import com.android04.godfisherman.utils.BindingAdapter
import com.android04.godfisherman.utils.StopwatchNotification
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {

    override val viewModel: MainViewModel by viewModels()
  
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StopwatchNotification.createChannel(this)
        initBottomNavigation()
        initMotionListener()

        viewModel.stopwatchOnFlag.observe(this) { flag ->
            if (flag) {
                // TODO 수정해야함
                // 라이브데이터 바인딩이 제대로 되지 않아서 일단 임시 방편으로 바인딩 어댑터를 직접 사용
                BindingAdapter.setVisibilityOnMotion(binding.clContainerStopwatch, flag)
                changeFragment(R.id.fl_stopwatch_big, TestStopwatchFragment())
                binding.container.setTransition(R.id.transition)
                binding.container.transitionToState(R.id.end)
            }
        }

        binding.textViewClose.setOnClickListener {
            binding.container.transitionToState(R.id.end_close)
            viewModel.stopwatchOnFlag.value = false
        }
    }

    private fun initBottomNavigation() {
        binding.navView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.navigation_home -> {
                    changeFragment(R.id.fl_fragment_container, HomeFragment())
                    viewModel.beforeMenuItemId = R.id.navigation_home
                    true
                }
                R.id.navigation_feed -> {
                    changeFragment(R.id.fl_fragment_container, FeedFragment())
                    viewModel.beforeMenuItemId = R.id.navigation_feed
                    true
                }
                R.id.navigation_camera -> {
                    startActivity(Intent(this, CameraActivity::class.java))
                    viewModel.beforeMenuItemId = R.id.navigation_camera
                    true
                }
                R.id.navigation_stopwatch -> {
                    if (viewModel.stopwatchOnFlag.value == true) {
                        binding.container.transitionToEnd()
                        viewModel.isFromStopwatchFragment = true
                    } else {
                        changeFragment(R.id.fl_fragment_container, StopwatchInfoFragment())
                    }
                    true
                }
                R.id.navigation_my_page -> {
                    changeFragment(R.id.fl_fragment_container, MyPageFragment())
                    viewModel.beforeMenuItemId = R.id.navigation_my_page
                    true
                }
                else -> false
            }
        }
        changeFragment(R.id.fl_fragment_container, HomeFragment())
        viewModel.beforeMenuItemId = R.id.navigation_home
    }

    private fun changeFragment(containerId: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(containerId, fragment).commit()
    }

    private fun initMotionListener() {
        binding.container.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {}

            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {}

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                if (viewModel.isFromInfoFragment) {
                    supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_container, HomeFragment()).commit()
                    viewModel.isFromInfoFragment = false
                    binding.navView.menu.findItem(R.id.navigation_home).isChecked = true
                }
                if (viewModel.isFromStopwatchFragment) {
                    binding.navView.menu.findItem(viewModel.beforeMenuItemId).isChecked = true
                    viewModel.isFromStopwatchFragment = false
                }
            }

            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {}
        })
    }
}

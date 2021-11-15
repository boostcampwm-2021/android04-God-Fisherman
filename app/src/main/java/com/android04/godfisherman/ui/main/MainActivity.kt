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
                // 라이브데이터 바인딩이 제대로 되지 않아서 일단 임시 방편으로 바인딩 어댑터를 직접 사용
                BindingAdapter.setVisibilityOnMotion(binding.clContainerStopwatch, flag)
                binding.container.transitionToEnd()
            }
        }
    }

    private fun initBottomNavigation() {
        binding.navView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.navigation_home -> {
                    changeFragment(R.id.fl_fragment_container, HomeFragment())
                    true
                }
                R.id.navigation_feed -> {
                    changeFragment(R.id.fl_fragment_container, FeedFragment())
                    true
                }
                R.id.navigation_camera -> {
                    startActivity(Intent(this, CameraActivity::class.java))
                    true
                }
                R.id.navigation_stopwatch -> {
                    // TODO 플래그를 두고 현재 스톱워치가 동작하는지 여부를 판단
                    if (true) // 원래는 스톱워치가 동작하고 있지 않은 경우임
                    changeFragment(R.id.fl_fragment_container, StopwatchInfoFragment())
                    true
                }
                R.id.navigation_my_page -> {
                    changeFragment(R.id.fl_fragment_container, MyPageFragment())
                    true
                }
                else -> false
            }
        }

        changeFragment(R.id.fl_fragment_container, HomeFragment())
    }

    private fun changeFragment(containerId: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(containerId, fragment).commit()
    }

    private fun initMotionListener() {
        binding.container.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {}

            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {}

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                Log.d("TAG", "onTransitionCompleted: Test!!!")
                if (viewModel.isFromInfoFragment) {
                    Log.d("TAG", "${viewModel.isFromInfoFragment}")
                    supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_container, HomeFragment()).commit()
                    viewModel.isFromInfoFragment = false
                    binding.navView.menu.findItem(R.id.navigation_home).isChecked = true
                }
            }

            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {}

        })

    }
}

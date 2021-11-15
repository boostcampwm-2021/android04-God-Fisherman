package com.android04.godfisherman.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.ActivityMainBinding
import com.android04.godfisherman.ui.base.BaseActivity
import com.android04.godfisherman.ui.camera.CameraActivity
import com.android04.godfisherman.ui.feed.FeedFragment
import com.android04.godfisherman.ui.home.HomeFragment
import com.android04.godfisherman.ui.mypage.MyPageFragment
import com.android04.godfisherman.utils.StopwatchNotification
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {

    override val viewModel: MainViewModel by viewModels()
  
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StopwatchNotification.createChannel(this)
        initBottomNavigation()
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
}

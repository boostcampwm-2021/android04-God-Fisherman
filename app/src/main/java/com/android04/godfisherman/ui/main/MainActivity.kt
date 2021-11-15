package com.android04.godfisherman.ui.main

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import com.android04.godfisherman.ui.base.BaseActivity
import com.android04.godfisherman.utils.StopwatchNotification

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {

    override val viewModel: MainViewModel by viewModels()
  
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObserver()
        viewModel.checkConnectivity()

        setOrientation()

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        StopwatchNotification.createChannel(this)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun setOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun setObserver() {
        viewModel.isNetworkConnected.observe(this) {
        }
    }
    
}

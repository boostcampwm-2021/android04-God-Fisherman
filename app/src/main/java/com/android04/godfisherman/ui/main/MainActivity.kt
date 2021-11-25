package com.android04.godfisherman.ui.main

import android.Manifest
import android.content.Intent
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
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
import com.android04.godfisherman.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {

    override val viewModel: MainViewModel by viewModels()
    private val swipeMotionLayoutWrapper: SwipeMotionLayoutWrapper by lazy {
        SwipeMotionLayoutWrapper(binding.container)
    }

    private lateinit var serviceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.activity = this
        serviceIntent = Intent(this, StopwatchService::class.java)
        registerReceiver(receiveTime, IntentFilter(StopwatchService.SERVICE_DESTROYED))

        setOrientation()
        StopwatchNotification.createChannel(this)
        checkLocationPermission()
        initBottomNavigation()
        initMotionListener()
        checkFromService()
        setupObserver()
    }

    fun closeContainer() {
        if (viewModel.isStopwatchStarted.value == true) {
            swipeMotionLayoutWrapper.transitionToState(R.id.end)
            viewModel.endStopwatch()
            showDialog()
        } else {
            swipeMotionLayoutWrapper.transitionToState(R.id.end_close)
            viewModel.stopwatchOnFlag.value = false
        }
    }

    private fun checkFromService() {
        val flag = intent.getBooleanExtra(StopwatchService.FROM_SERVICE, false)
        MainViewModel.isFromService = flag
        if (flag) viewModel.stopwatchOnFlag.value = true
    }

    private fun setupObserver() {
        viewModel.isAfterUpload.observe(this) {
            if (it) {
                swipeMotionLayoutWrapper.transitionToState(R.id.end_close)
                viewModel.stopwatchOnFlag.value = false
                viewModel.setIsAfterUploadFalse()
                viewModel.isOpened = false
            }
        }

        viewModel.currentLocation.observe(this) {
            if (it != null) {
                supportFragmentManager.setFragmentResult(
                    HomeFragment.LOCATION_UPDATED, bundleOf(
                        DEFAULT_BUNDLE to true
                    )
                )
            }
        }

        viewModel.stopwatchOnFlag.observe(this) { flag ->
            if (flag) {
                changeFragment(R.id.fl_stopwatch_big, TestStopwatchFragment())
                swipeMotionLayoutWrapper.apply {
                    setTransition(R.id.transition)
                    swipeMotionLayoutWrapper.setProgress(1f) {
                        viewModel.isOpened = true
                        binding.navView.menu.findItem(viewModel.beforeMenuItemId).isChecked = true
                    }
                }
            }
            val visibility = if (flag) View.VISIBLE else View.GONE
            swipeMotionLayoutWrapper.updateConstraintSet { constraintSet ->
                constraintSet.setVisibility(R.id.cl_container_stopwatch, visibility)
            }
        }
    }

    private fun initBottomNavigation() {
        binding.navView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
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
                    viewModel.isServiceRequestWithOutCamera = false
                    startActivity(Intent(this, CameraActivity::class.java))
                    viewModel.beforeMenuItemId = R.id.navigation_camera
                    true
                }
                R.id.navigation_stopwatch -> {
                    if (viewModel.stopwatchOnFlag.value == true) {
                        swipeMotionLayoutWrapper.transitionToState(R.id.end)
                    } else {
                        changeFragmentWithBackStack(R.id.fl_fragment_container, StopwatchInfoFragment())
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

    private fun changeFragmentWithBackStack(containerId: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .addToBackStack(BEFORE_FRAGMENT)
            .commit()
    }

    fun removeFragment() {
        supportFragmentManager.popBackStack()
    }

    private fun initMotionListener() {
        swipeMotionLayoutWrapper.setupTransitionListener(
            transitionCompletedCallback = { _, currentId ->
                binding.navView.menu.findItem(viewModel.beforeMenuItemId).isChecked = true
                MainViewModel.isFromService = false

                when (currentId) {
                    R.id.end -> {
                        viewModel.isOpened = true
                        viewModel.loadTmpTimeLineRecord()
                    }
                    else -> {
                        viewModel.isOpened = false
                    }
                }
            },
            transitionStartedCallback = null,
            transitionChangedCallback = null,
            transitionTriggerCallback = null
        )
    }

    override fun onBackPressed() {
        var isHome = false
        supportFragmentManager.fragments.forEach {
            if (it is HomeFragment) {
                isHome = true
            }
        }

        println(supportFragmentManager.fragments)

        when {
            viewModel.isOpened -> {
                swipeMotionLayoutWrapper.transitionToState(R.id.start)
                viewModel.isOpened = false
            }
            !isHome -> {
                transitionToHome()
            }
            isHome -> {
                backWithFinish()
            }
        }
    }

    private fun transitionToHome() {
        changeFragment(R.id.fl_fragment_container, HomeFragment())
        viewModel.beforeMenuItemId = R.id.navigation_home
        binding.navView.selectedItemId = R.id.navigation_home
    }

    private fun backWithFinish() {
        val currentTime = System.currentTimeMillis()

        if (currentTime > viewModel.lastBackTime + 2000) {
            showToast(this, "한 번 더 누르면 앱을 종료합니다.")
            viewModel.lastBackTime = currentTime
        } else {
            finish()
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun setOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onStop() {
        super.onStop()
        if (viewModel.isServiceRequestWithOutCamera) {
            passStopwatchToService(viewModel.stopwatch.getTime())
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.isServiceRequestWithOutCamera = true
        if (isStopwatchServiceRunning) {
            stopService(serviceIntent)
            isStopwatchServiceRunning = false
        }
    }

    private fun passStopwatchToService(time: Double) {
        if (viewModel.isStopwatchStarted.value == true) {
            viewModel.passStopwatchToService()
            serviceIntent.putExtra(StopwatchService.TIME_EXTRA, time)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent)
            } else {
                startService(serviceIntent)
            }
        }
    }

    private val receiveTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, receiveIntent: Intent) {
            val receivedTime = receiveIntent.getDoubleExtra(StopwatchService.SERVICE_DESTROYED, 0.0)
            viewModel.passedTimeFromService(receivedTime)
            NotificationManagerCompat.from(context).cancel(StopwatchService.NOTIFICATION_ID)
        }
    }

    private fun showDialog() {
        val dialog = UploadDialog(this, { viewModel.saveTimeLineRecord() },
            { viewModel.resumeStopwatch() })
        dialog.showDialog()
    }

    private fun checkLocationPermission() {
        if (isGrantedLocationPermission(this)) {
            viewModel.requestLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        var permissionCount = 0
        val permissionManager = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(
            )
        ) { permissions ->
            permissions.entries.forEach {
                if (it.value) permissionCount++
            }
            if (permissionCount == 2) {
                viewModel.requestLocation()
            } else {
                finish()
            }
        }
        permissionManager.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    fun setMotionSwipeAreaVisibility(visibility: Int) {
        swipeMotionLayoutWrapper.updateConstraint(R.id.end, R.id.cl_container_stopwatch) {
            it.propertySet.visibility = visibility
        }
    }

    companion object {
        const val DEFAULT_BUNDLE = "defaultKey"
        const val BEFORE_FRAGMENT = "before"
        var isStopwatchServiceRunning = false
    }
}

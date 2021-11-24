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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.MotionLayout
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
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {

    override val viewModel: MainViewModel by viewModels()

    companion object {
        const val DEFAULT_BUNDLE = "defaultKey"
        var isStopwatchServiceRunning = false
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
            binding.container.transitionToState(R.id.end)
            viewModel.endStopwatch()
            showDialog()
        } else {
            binding.container.transitionToState(R.id.end_close)
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
                binding.container.transitionToState(R.id.end_close)
                viewModel.stopwatchOnFlag.value = false
                binding.navView.selectedItemId = R.id.navigation_stopwatch
                changeFragment(R.id.fl_fragment_container, StopwatchInfoFragment())
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
                // TODO 수정해야함
                // 라이브데이터 바인딩이 제대로 되지 않아서 일단 임시 방편으로 바인딩 어댑터를 직접 사용
                BindingAdapter.setVisibilityOnMotion(binding.clContainerStopwatch, flag)
                changeFragment(R.id.fl_stopwatch_big, TestStopwatchFragment())
                binding.container.setTransition(R.id.transition)
                binding.container.transitionToState(R.id.end)
                val marginInPx = resources.getDimension(R.dimen.stopwatch_view_height_small)
                setMarginBottomInMotion(marginInPx)
            } else {
                BindingAdapter.setVisibilityOnMotion(binding.clContainerStopwatch, flag)
                setMarginBottomInMotion(0f)
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
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

                if (viewModel.isFromInfoFragment) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_fragment_container, HomeFragment()).commit()
                    viewModel.isFromInfoFragment = false
                    binding.navView.menu.findItem(R.id.navigation_home).isChecked = true
                }
                if (viewModel.isFromStopwatchFragment) {
                    binding.navView.menu.findItem(viewModel.beforeMenuItemId).isChecked = true
                    viewModel.isFromStopwatchFragment = false
                }
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
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }
        })
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
                binding.container.transitionToState(R.id.start)
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
        val constraintSet = binding.container.getConstraintSet(R.id.end)
        val swipeArea = constraintSet.getConstraint(R.id.cl_container_stopwatch)
        swipeArea.propertySet.visibility = visibility
    }

    private fun setMarginBottomInMotion(marginInPx: Float) {
        val constraintSet = binding.container.getConstraintSet(R.id.start)
        val frameLayoutConstraint = constraintSet.getConstraint(R.id.fl_fragment_container)
        frameLayoutConstraint.layout.bottomMargin = marginInPx.roundToInt()
    }
}

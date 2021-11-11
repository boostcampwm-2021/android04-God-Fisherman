package com.android04.godfisherman.ui.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.ActivityStopwatchBinding
import com.android04.godfisherman.ui.base.BaseActivity
import com.android04.godfisherman.utils.StopwatchService
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.android04.godfisherman.ui.main.MainActivity
import com.android04.godfisherman.utils.RecyclerViewEmptySupport
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StopwatchActivity :
    BaseActivity<ActivityStopwatchBinding, StopwatchViewModel>(R.layout.activity_stopwatch) {
    companion object {
        var isStopwatchServiceRunning = false
    }

    val dummy = TimeLineDataTest("00 : 16 : 27", "방어", "123.12", "상주은모래비치")
    val dummyList = arrayListOf(dummy, dummy, dummy, dummy, dummy, dummy, dummy, dummy, dummy, dummy)

    override val viewModel: StopwatchViewModel by viewModels()
    private lateinit var serviceIntent: Intent
    private var isPlayAnimate = false
    private var isFromService = false

    private val receiveTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, receiveIntent: Intent) {
            val receivedTime = receiveIntent.getDoubleExtra(StopwatchService.SERVICE_DESTROYED, 0.0)
            Log.d("receiveFromService", "리시브 실행 : $receivedTime")
            viewModel.passedTimeFromService(receivedTime)
            isFromService = true
            NotificationManagerCompat.from(context).cancel(StopwatchService.NOTIFICATION_ID)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        serviceIntent = Intent(applicationContext, StopwatchService::class.java)
        registerReceiver(receiveTime, IntentFilter(StopwatchService.SERVICE_DESTROYED))
        setRV()
        setObserver()
    }
    private fun setRV(){
        val recyclerViewEmptySupport = binding.rvTimeLine
        val emptyView = binding.tvEmptyView
        recyclerViewEmptySupport.adapter = TimelineListAdapter()
        recyclerViewEmptySupport.setEmptyView(emptyView)
        recyclerViewEmptySupport.setVerticalInterval(50)
        recyclerViewEmptySupport.submitList(dummyList)
    }
    override fun onResume() {
        super.onResume()
        Log.d("serviceRunning", "$isStopwatchServiceRunning")
        if (isStopwatchServiceRunning) {
            Log.d("serviceRunning", "서비스 종료 실행")
            stopService(serviceIntent)
            isStopwatchServiceRunning = false
        }
    }

    private fun setObserver(){
        viewModel.isStopwatchStarted.observe(this, Observer {
            binding.vShadow.isVisible = it
            isPlayAnimate = it
            if(it){
                animateShadow()
            }else{

            }
        })
    }

    private fun animateShadow() {
        if(isPlayAnimate){
            Log.d("animateShadow", "메소드 실행")
            binding.vShadow.apply {
                animate().scaleX(1.1f).scaleY(1.1f).setDuration(1000).withEndAction {
                    animate().scaleX(1f).scaleY(1f).setDuration(1000).withEndAction {
                        animateShadow()
                    }.start()
                }.start()
            }
        }
    }

    private fun passStopwatchToService(time: Double) {
        if (viewModel.isStopwatchStarted.value == true) {
            serviceIntent.putExtra(StopwatchService.TIME_EXTRA, time)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent)
            } else {
                startService(serviceIntent)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        passStopwatchToService(viewModel.time)
    }

    override fun onBackPressed() {
        if(isFromService){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            finish()
        }
    }
}
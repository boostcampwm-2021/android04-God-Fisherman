package com.android04.godfisherman.ui.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.android04.godfisherman.R

class StopwatchActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stopwatch)

        // RecyclerView 테스트
        val recyclerView = findViewById<RecyclerView>(R.id.rv_time_line)
        val adapter = TimelineListAdapter()
        recyclerView.adapter = adapter
        val dummy = TimeLineDataTest("00 : 16 : 27", "방어", "123.12", "상주은모래비치")
        val dummyList = arrayListOf(dummy, dummy, dummy, dummy, dummy, dummy, dummy, dummy, dummy, dummy)
        adapter.submitList(dummyList)
    }
}

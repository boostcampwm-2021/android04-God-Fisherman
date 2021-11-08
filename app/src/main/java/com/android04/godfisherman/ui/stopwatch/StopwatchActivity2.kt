package com.android04.godfisherman.ui.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android04.godfisherman.R
import com.android04.godfisherman.utils.RecyclerViewEmptySupport

class StopwatchActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stopwatch)

        // RecyclerView 테스트
        val recyclerViewEmptySupport = findViewById<RecyclerViewEmptySupport>(R.id.rv_time_line)
        val emptyView = findViewById<TextView>(R.id.tv_empty_view)


        val dummy = TimeLineDataTest("00 : 16 : 27", "방어", "123.12", "상주은모래비치")
        val dummyList = arrayListOf(dummy, dummy, dummy, dummy, dummy, dummy, dummy, dummy, dummy, dummy)
        // val emptyList = arrayListOf<TimeLineDataTest>()

        recyclerViewEmptySupport.adapter = TimelineListAdapter()
        recyclerViewEmptySupport.setEmptyView(emptyView)
        recyclerViewEmptySupport.setVerticalInterval(50)

        // recyclerViewEmptySupport.submitList(emptyList)
        recyclerViewEmptySupport.submitList(dummyList)
    }
}

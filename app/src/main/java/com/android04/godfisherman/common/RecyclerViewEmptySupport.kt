package com.android04.godfisherman.common

import android.content.Context
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.ListAdapter
import java.lang.Exception


class RecyclerViewEmptySupport : RecyclerView {

    constructor (context: Context) : super(context){}

    constructor (context: Context, attrs: AttributeSet) : super(context, attrs){}

    private var emptyView: View? = null

    private fun onDataChanged() {
        if (adapter != null && emptyView != null)
        {
            if (adapter!!.itemCount == 0)
            {
                emptyView!!.visibility = View.VISIBLE
                this@RecyclerViewEmptySupport.visibility = View.GONE
            } else
            {
                emptyView!!.visibility = View.GONE
                this@RecyclerViewEmptySupport.visibility = View.VISIBLE
            }
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        onDataChanged()
    }

    fun setEmptyView(emptyView: View) {
        this.emptyView = emptyView
    }

    fun <T> submitList(dataList: List<T>) {
        try {
            (adapter as ListAdapter<T, *>).submitList(dataList) {
                onDataChanged()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setVerticalInterval(height: Int) {
        this.addItemDecoration(VerticalItemDecoration(height))
    }

    fun setConfiguration(adapter: Adapter<*>?, emptyView: View, interval: Int) {
        setAdapter(adapter)
        setEmptyView(emptyView)
        setVerticalInterval(interval)
    }

    class VerticalItemDecoration(private val height: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView, state: State
        ) {
            outRect.top = height
            parent.adapter?.let { parentAdapter ->
                if (parent.getChildAdapterPosition(view) == parentAdapter.itemCount - 1) {
                    outRect.bottom = height
                }
            }
        }
    }
}

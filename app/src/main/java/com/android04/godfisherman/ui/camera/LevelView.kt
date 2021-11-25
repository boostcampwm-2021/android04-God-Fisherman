package com.android04.godfisherman.ui.camera

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.SensorEvent
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.android04.godfisherman.R

class LevelView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    val innerPaint = Paint()
    val outerPaint = Paint()
    val guidePaint = Paint()

    private var centerX = 0f
    private var centerY = 0f
    private var sensorX = 0f
    private var sensorY = 0f

    override fun onSizeChanged(newW: Int, newH: Int, oldW: Int, oldH: Int) {
        centerX = (newW / 3f) * 1
        centerY = (newH / 3f) * 2
    }

    override fun onDraw(canvas: Canvas?) {
        innerPaint.color = ContextCompat.getColor(context, R.color.primary)
        outerPaint.color = Color.WHITE
        outerPaint.style = Paint.Style.STROKE
        outerPaint.strokeWidth = 4f
        guidePaint.color = Color.WHITE
        guidePaint.style = Paint.Style.STROKE
        guidePaint.strokeWidth = 4f

        canvas?.drawCircle(centerX, centerY, centerX * 0.5f, outerPaint)
        canvas?.drawCircle(sensorX + centerX, sensorY + centerY, centerX * 0.25f - 3f, innerPaint)
        canvas?.drawCircle(centerX, centerY, centerX * 0.25f, guidePaint)
    }

    fun onSensorEvent(event: SensorEvent) {
        sensorY = event.values[0] * 10
        sensorX = event.values[1] * 10
        invalidate()
    }
}
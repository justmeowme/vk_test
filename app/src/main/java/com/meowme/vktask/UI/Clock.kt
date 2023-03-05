package com.meowme.vktask.UI

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.lang.Math.PI
import java.util.Calendar
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class Clock @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLACK
        strokeWidth = 5F
        isAntiAlias = true
    }

    private var size = min(width, height)
    private var radius = size * 1f
    private var paddingText = 64f
    private var textSize = 40f

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        radius = (min(width, height)) / 2.0f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //draw main circle
        paint.style = Paint.Style.FILL
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)

        //draw center circle
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), 16f, paint)

        //draw digits
        paint.textSize = textSize
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        var angle = 0f
        val items = arrayListOf<String>("4", "5", "6", "7", "8", "9", "10", "11", "12", "1", "2", "3");
        for (item in items){
            angle = (angle + PI / 6).toFloat()
            val x = ((radius - paddingText) * cos(angle)) + radius - 16
            val y = ((radius - paddingText) * sin(angle)) + radius + 16
            canvas.drawText(item, x, y, paint)
        }

        //get current time
        val c = Calendar.getInstance()
        val seconds = c.get(Calendar.SECOND)
        val minutes = c.get(Calendar.MINUTE)
        var hours = c.get(Calendar.HOUR)
        if (hours > 12){
            hours -= 12
        }

        //draw arrows
        drawLine(canvas, seconds.toFloat(), Color.WHITE, 5F, (radius * 0.9).toFloat())
        drawLine(canvas, minutes.toFloat(), Color.GRAY, 12F, (radius * 0.7).toFloat())
        drawLine(canvas, (hours + minutes / 60) * 5f, Color.RED, 12F, (radius * 0.4).toFloat())

        //redraw every second
        postInvalidateDelayed(1000)
        invalidate()
    }

    private fun drawLine(canvas: Canvas, x: Float, color: Int, lineWidth: Float, lineHeight: Float){
        paint.color = color
        paint.strokeWidth = lineWidth
        val angle = (PI * x / 30 - PI / 2).toFloat()
        canvas.drawLine(width / 2f, height / 2f,
            (lineHeight * cos(angle) + radius).toFloat(),
            (lineHeight * sin(angle) + radius).toFloat(), paint)
    }

}
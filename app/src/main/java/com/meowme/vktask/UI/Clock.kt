package com.meowme.vktask.UI

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.meowme.vktask.R
import java.lang.Math.PI
import java.util.Calendar
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class Clock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var size = min(width, height)
    private var radius = size * 1f
    private var paddingText = 64f

    private var clockColor = Color.BLACK
    private var digitsColor = Color.WHITE
    private var secondsArrowColor = Color.WHITE
    private var digitsStyle = "arabic"
    private var form = "circle"

    init {
        context.withStyledAttributes(attrs, R.styleable.Clock) {
            clockColor = getColor(R.styleable.Clock_clockColor, Color.BLACK)
            digitsColor = getColor(R.styleable.Clock_digitsColor, Color.WHITE)
            secondsArrowColor = getColor(R.styleable.Clock_secondsArrowColor, Color.WHITE)
            digitsStyle = getString(R.styleable.Clock_digitsType).toString()
            form = getString(R.styleable.Clock_form).toString()
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        radius = (min(width, height)) / 2.0f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //draw main circle
        paint.style = Paint.Style.FILL
        paint.color = clockColor
        paint.isAntiAlias = true
        if (form=="square"){
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        } else{
            canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)
        }

        //draw center circle
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), 16f, paint)

        //draw digits
        paint.textSize = radius / 7
        paint.color = digitsColor
        paint.style = Paint.Style.FILL
        var angle = 0f

        val items = if (digitsStyle=="arabic"){
            arrayListOf("4", "5", "6", "7", "8", "9", "10", "11", "12", "1", "2", "3")
        } else{
            arrayListOf("IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "I", "II", "III")
        }

        paddingText = radius / 7
        for (item in items) {
            angle = (angle + PI / 6).toFloat()
            val x = ((radius - paddingText) * cos(angle)) + radius - 16
            val y = ((radius - paddingText) * sin(angle)) + radius + 16
            canvas.drawText(item, x, y, paint)
        }

        //get current time
        val c = Calendar.getInstance()
        val seconds = c.get(Calendar.SECOND)
        val minutes = c.get(Calendar.MINUTE)
        val hours = c.get(Calendar.HOUR)


        //draw arrows
        drawLine(canvas, seconds.toFloat(), secondsArrowColor, 5F, (radius * 0.9).toFloat())
        drawLine(canvas, minutes.toFloat(), Color.GRAY, 12F, (radius * 0.7).toFloat())
        drawLine(canvas, hours * 5f, Color.RED, 12F, (radius * 0.4).toFloat())

        //redraw every second
        postInvalidateDelayed(1000)
        invalidate()
    }

    private fun drawLine(
        canvas: Canvas,
        x: Float,
        color: Int,
        lineWidth: Float,
        lineHeight: Float
    ) {
        paint.color = color
        paint.strokeWidth = lineWidth
        val angle = (PI * x / 30 - PI / 2).toFloat()
        canvas.drawLine(
            width / 2f, height / 2f,
            (lineHeight * cos(angle) + radius),
            (lineHeight * sin(angle) + radius), paint
        )
    }

    public fun changeClockColor(newColor: Int){
        clockColor = newColor
    }

    public fun changeSecondsArrowColor(newArrowColor: Int){
        secondsArrowColor = newArrowColor
    }

    public fun changeDigitsColor(newDigitsColor: Int){
        digitsColor = newDigitsColor
    }

    public fun changeDigitsStyle(newStyle: String){
        digitsStyle = newStyle
    }

    public fun changeForm(newForm: String){
        form = newForm
    }

}
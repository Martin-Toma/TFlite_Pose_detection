package com.example.tfliteposedetection

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.View

class Draw(context: Context?, private val circleX: Float, private val circleY: Float, private val i_width: Int, private val i_height: Int): View(context) {
    lateinit var boundaryPaint: Paint
    lateinit var textPaint: Paint

    lateinit var paintCircle: Paint
    val radius: Float = 20.0F

    init {
        init()
    }

    private fun init(){
        paintCircle = Paint()
        paintCircle.setStyle(Paint.Style.FILL)
        paintCircle.color = Color.CYAN
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if(canvas != null) {
            val transformedX = (width - circleX) * (width / i_width) // * (width / previewX) // / 720) * previewX
            val transformedY = circleY * (height / i_height) // * (height / previewY) // / 1280) * previewY

            Log.d("Position", "x = ${x} y = ${y}")
            canvas.drawCircle(transformedX, transformedY, radius, paintCircle)

            canvas.drawCircle(0F, 0F, radius, paintCircle)
            canvas.drawCircle(0F, canvas.height.toFloat(), radius, paintCircle)
            canvas.drawCircle(width.toFloat(), 0F, radius, paintCircle)
            canvas.drawCircle(width.toFloat(), canvas.height.toFloat(), radius, paintCircle)
        }

    }
}
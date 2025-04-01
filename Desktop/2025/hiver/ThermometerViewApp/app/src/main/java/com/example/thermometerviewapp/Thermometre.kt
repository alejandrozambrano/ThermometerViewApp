package com.example.thermometerviewapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class Thermometre(context: Context) : View(context) {

    private var temp: Float = 0f
    private val paint = Paint()

    fun setTemp(value: Float) {
        temp = value
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        canvas.drawColor(Color.WHITE)

        val minTempC = 0
        val maxTempC = 50
        val step = 5

        paint.color = Color.BLACK
        paint.textSize = 30f
        paint.strokeWidth = 4f

        val offsetLeft = 180f
        val offsetRight = 100f

        for (i in minTempC..maxTempC step step) {
            val y = height - (i / maxTempC.toFloat()) * height

            canvas.drawLine(width / 2 - 40, y, width / 2 - 20, y, paint)
            canvas.drawLine(width / 2 + 20, y, width / 2 + 40, y, paint)

            val f = i * 9 / 5 + 32
            canvas.drawText("${f}°F", width / 2 - offsetLeft, y + 10, paint)
            canvas.drawText("${i}°C", width / 2 + offsetRight, y + 10, paint)
        }

        // Indicador
        paint.style = Paint.Style.FILL
        paint.color = if (temp < 25) Color.BLUE else Color.RED

        val barHeight = (temp / maxTempC) * height
        canvas.drawRect(width / 2 - 30, height - barHeight, width / 2 + 30, height, paint)

        // Bulbo
        canvas.drawCircle(width / 2, height, 50f, paint)

        // Títulos verticales
        // Títulos verticales mejorados
        paint.color = Color.DKGRAY
        paint.textSize = 50f  // más grande
        paint.isFakeBoldText = true // más grueso

        // Fahrenheit (°F)
        canvas.save()
        canvas.rotate(-90f, 60f, height / 2)
        canvas.drawText("Fahrenheit (°F)", 60f, height / 2, paint)
        canvas.restore()

        // Celsius (°C)
        canvas.save()
        canvas.rotate(90f, width - 60f, height / 2)
        canvas.drawText("Celsius (°C)", width - 60f, height / 2, paint)
        canvas.restore()

    }

}

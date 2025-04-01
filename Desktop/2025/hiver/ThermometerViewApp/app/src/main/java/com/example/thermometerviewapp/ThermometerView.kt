package com.example.thermometerviewapp
// Este archivo define una vista personalizada (es decir, un dibujo) del termómetro.
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class ThermometerView(context: Context) : View(context) {
    //Extendemos la clase View para poder dibujar cosas en pantalla con Canvas.
    private var tempCelsius: Float = 20f
    // Es la temperatura actual que queremos mostrar. Empezamos en 20°C por defecto.




    fun setTemp(temp: Float) {
        this.tempCelsius = temp
        invalidate()
        //Guardamos el nuevo valor de temperatura y pedimos que se vuelva a
    // dibujar la vista (llama automáticamente a onDraw).
    }

    override fun onDraw(canvas: Canvas) {
        //Aquí es donde dibujamos el termómetro visualmente.
        super.onDraw(canvas)

        val tempFahrenheit = tempCelsius * 9 / 5 + 32
        //Convertimos la temperatura a Fahrenheit para mostrar ambos valores.
        val paint = Paint()

        // Color
        paint.color = if (tempCelsius < 25) Color.BLUE else Color.RED
        // Si la temperatura es menor a 25°C → azul
        // Si es 25°C o más → rojo

        // Termómetro
        val centerX = width / 2f
        val bottomY = height - 200f
        val topY = bottomY - (tempCelsius * 10)

        canvas.drawLine(centerX, topY, centerX, bottomY, paint)
        canvas.drawCircle(centerX, bottomY, 60f, paint)
        //Dibujamos la columna de "mercúrio" del termómetro y la base circular.

        // Etiquetas
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 50f
            textAlign = Paint.Align.CENTER
        }

        canvas.drawText("Temp: %.1f°C / %.1f°F".format(tempCelsius, tempFahrenheit), centerX, 100f, textPaint)
        //Mostramos la temperatura en la parte superior de la pantalla.
    }
}

package com.example.thermometerviewapp

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.Button
import android.content.Intent

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var tempSensor: Sensor? = null
    private lateinit var t: Thermometre
    private lateinit var dbHelper: TemperatureDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        dbHelper = TemperatureDatabaseHelper(this)

        // Inicializar el termómetro
        t = Thermometre(this)

        // Crear botón "Voir Historique"
        val btn = Button(this)
        btn.text = "Voir Historique"
        btn.setOnClickListener {
            val intent = Intent(this, HistoriqueActivity::class.java)
            startActivity(intent)
        }

        // Layout principal
        val layout = FrameLayout(this)
        layout.addView(t)

        // Posicionar el botón abajo a la derecha
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.BOTTOM or Gravity.END
        params.marginEnd = 32
        params.bottomMargin = 32
        btn.layoutParams = params

        layout.addView(btn)
        setContentView(layout)

        // Obtener el sensor de temperatura
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        if (tempSensor != null) {
            sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            Toast.makeText(this, "Temperature sensor not available", Toast.LENGTH_LONG).show()
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            val tempC = event.values[0]
            val tempF = tempC * 9 / 5 + 32
            t.setTemp(tempC)

            val timestamp = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())
            dbHelper.insertTemperature(tempC, tempF, timestamp)

            Toast.makeText(this, "Temp: $tempC°C  |  $tempF°F", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No usado
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        tempSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
}


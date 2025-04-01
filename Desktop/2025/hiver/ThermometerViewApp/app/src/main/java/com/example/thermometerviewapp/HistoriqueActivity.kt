package com.example.thermometerviewapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class HistoriqueActivity : AppCompatActivity() {

    private lateinit var dbHelper: TemperatureDatabaseHelper
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val dataList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listView = ListView(this)

        val btnBack = Button(this)
        btnBack.text = "Retour au Thermomètre"
        btnBack.setOnClickListener {
            finish()
        }

        val btnClear = Button(this)
        btnClear.text = "Effacer l'historique"
        btnClear.setOnClickListener {
            dbHelper.deleteAllTemperatures()
            dataList.clear()
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Historique effacé", Toast.LENGTH_SHORT).show()
        }

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(btnBack)
        layout.addView(btnClear)
        layout.addView(listView)

        setContentView(layout)

        dbHelper = TemperatureDatabaseHelper(this)
        loadTemperatures()
    }

    private fun loadTemperatures() {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM temperatures ORDER BY id DESC", null)
        dataList.clear()

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        if (cursor.moveToFirst()) {
            do {
                val tempC = cursor.getFloat(cursor.getColumnIndexOrThrow("temp_c"))
                val tempF = cursor.getFloat(cursor.getColumnIndexOrThrow("temp_f"))
                val timestampStr = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"))

                val timestampLong = timestampStr.toLongOrNull()
                val formattedDate = if (timestampLong != null) {
                    dateFormat.format(Date(timestampLong))
                } else {
                    "???"
                }

                dataList.add("🕒 $formattedDate\n🌡 %.2f°C | %.2f°F".format(tempC, tempF))
            } while (cursor.moveToNext())
        }
        cursor.close()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dataList)
        listView.adapter = adapter
    }
}

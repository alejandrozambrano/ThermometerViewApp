package com.example.thermometerviewapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TemperatureDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, "temperature.db", null, 1
) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE temperatures (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                temp_c REAL,
                temp_f REAL,
                timestamp TEXT
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS temperatures")
        onCreate(db)
    }

    fun insertTemperature(tempC: Float, tempF: Float, timestamp: String) {
        val values = ContentValues().apply {
            put("temp_c", tempC)
            put("temp_f", tempF)
            put("timestamp", timestamp)
        }
        writableDatabase.insert("temperatures", null, values)
    }

    fun deleteAllTemperatures() {
        writableDatabase.execSQL("DELETE FROM temperatures")
    }



}

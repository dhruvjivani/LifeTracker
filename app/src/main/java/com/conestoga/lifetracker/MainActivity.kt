package com.conestoga.lifetracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var btnWeather: Button
    private lateinit var btnDatabase: Button
    private lateinit var btnLocation: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnWeather = findViewById(R.id.btnWeather)
        btnDatabase = findViewById(R.id.btnDatabase)
        btnLocation = findViewById(R.id.btnLocation)

        btnWeather.setOnClickListener {
            startActivity(Intent(this, WeatherActivity::class.java))
        }
        btnDatabase.setOnClickListener {
            startActivity(Intent(this, DatabaseActivity::class.java))
        }
        btnLocation.setOnClickListener {
            startActivity(Intent(this, LocationActivity::class.java))
        }
    }
}
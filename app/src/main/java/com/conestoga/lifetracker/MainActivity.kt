package com.conestoga.lifetracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnWeather = findViewById<Button>(R.id.btnWeather)
        val btnDatabase = findViewById<Button>(R.id.btnDatabase)
        val btnLocation = findViewById<Button>(R.id.btnLocation)
        val themeSwitch = findViewById<Switch>(R.id.switchTheme)

        val sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val isDark = sharedPref.getBoolean("dark_theme", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
        themeSwitch.isChecked = isDark

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPref.edit()
            editor.putBoolean("dark_theme", isChecked)
            editor.apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

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
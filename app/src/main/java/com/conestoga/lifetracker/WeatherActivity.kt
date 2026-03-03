package com.conestoga.lifetracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Activity for displaying weather information.
 * Fetches weather data from OpenWeatherMap API using Retrofit.
 * Note: Contains intentional bugs for PROG1145 assignment.
 */
class WeatherActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var cityInput: EditText
    private lateinit var btnFetch: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        cityInput = findViewById(R.id.editCityInput)
        btnFetch = findViewById(R.id.btnFetchWeather)
        recyclerView = findViewById(R.id.weatherRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnFetch.setOnClickListener {
            val city = cityInput.text.toString()
            if (city.isNotBlank()) {
                fetchWeather(city)
            } else {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Fetch weather data from API for the given city.
     * 
     * @param city The city name to fetch weather for
     */
    private fun fetchWeather(city: String) {
        val api = RetrofitClient.instance.create(WeatherAPI::class.java)
        // TODO: Replace with your actual API key from OpenWeatherMap
        val call = api.getWeather(city, "d8689c616a725f7030af006193e3e201")

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherData = response.body()
                    if (weatherData != null) {
                        // BUG #1: Intentional NullPointerException
                        // weatherData.weather could be null, causing crash on empty response
                        // This will crash if weather list is null
                        val weatherList = weatherData.weather!!
                        
                        if (weatherList.isEmpty()) {
                            Toast.makeText(this@WeatherActivity, "No weather data found", Toast.LENGTH_SHORT).show()
                        } else {
                            weatherAdapter = WeatherAdapter(weatherList)
                            recyclerView.adapter = weatherAdapter
                        }
                    } else {
                        Toast.makeText(this@WeatherActivity, "Empty response from server", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@WeatherActivity, "Failed to retrieve weather: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(this@WeatherActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

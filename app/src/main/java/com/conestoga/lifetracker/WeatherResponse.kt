package com.conestoga.lifetracker

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the weather API response from OpenWeatherMap.
 * Note: This contains intentional bug #1 - potential NullPointerException.
 */
data class WeatherResponse(
    @SerializedName("coord")
    val coord: Coordinate? = null,
    @SerializedName("weather")
    val weather: List<WeatherItem>? = null,
    @SerializedName("main")
    val main: MainWeatherData? = null,
    @SerializedName("wind")
    val wind: WindData? = null,
    @SerializedName("clouds")
    val clouds: CloudData? = null,
    @SerializedName("name")
    val cityName: String? = null
)

data class Coordinate(
    val lon: Double,
    val lat: Double
)

data class WeatherItem(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class MainWeatherData(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

data class WindData(
    val speed: Double,
    val deg: Int? = null,
    val gust: Double? = null
)

data class CloudData(
    val all: Int
)


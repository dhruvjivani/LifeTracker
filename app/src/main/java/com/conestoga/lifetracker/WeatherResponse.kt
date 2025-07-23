package com.conestoga.lifetracker

data class WeatherResponse(
    val weather: List<WeatherItem>
)

data class WeatherItem(
    val description: String
)

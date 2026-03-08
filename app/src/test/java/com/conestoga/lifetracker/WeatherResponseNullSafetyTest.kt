package com.conestoga.lifetracker

import org.junit.Assert.assertNull
import org.junit.Test

class WeatherResponseNullSafetyTest {

    @Test
    fun testWeatherResponse_AllFieldsNull() {
        val response = WeatherResponse(
            coord = null,
            weather = null,
            main = null,
            wind = null,
            clouds = null,
            cityName = null
        )
        
        assertNull(response.coord)
        assertNull(response.weather)
        assertNull(response.main)
        assertNull(response.wind)
        assertNull(response.clouds)
        assertNull(response.cityName)
    }

    @Test
    fun testWindData_OptionalFieldsNull() {
        val wind = WindData(speed = 10.0, deg = null, gust = null)
        assertNull(wind.deg)
        assertNull(wind.gust)
    }
}

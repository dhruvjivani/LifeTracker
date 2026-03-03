package com.conestoga.lifetracker

import com.conestoga.lifetracker.data.entity.NoteEntity
import com.conestoga.lifetracker.data.repository.NoteRepository
import com.conestoga.lifetracker.domain.model.Note
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

/**
 * Unit tests for JSON parsing from weather API responses.
 * Tests positive, negative, and edge cases for JSON deserialization.
 */
class WeatherJsonParsingTest {
    private lateinit var gson: Gson

    @Before
    fun setUp() {
        gson = Gson()
    }

    /**
     * Test Case 1: Positive case - Valid complete weather response
     * Verifies that a properly formatted JSON is correctly parsed
     */
    @Test
    fun testParseValidWeatherResponse() {
        val json = """{
            "coord": {"lon": 10.0, "lat": 50.0},
            "weather": [
                {
                    "id": 800,
                    "main": "Clear",
                    "description": "clear sky",
                    "icon": "01d"
                }
            ],
            "main": {
                "temp": 20.5,
                "feels_like": 19.2,
                "temp_min": 18.0,
                "temp_max": 22.0,
                "pressure": 1013,
                "humidity": 65
            },
            "wind": {
                "speed": 5.5,
                "deg": 230
            },
            "clouds": {"all": 10},
            "name": "Berlin"
        }"""

        val response = gson.fromJson(json, WeatherResponse::class.java)

        assertNotNull("Weather response should not be null", response)
        assertEquals("City name should be Berlin", "Berlin", response.cityName)
        assertEquals("Weather list size should be 1", 1, response.weather?.size)
        assertEquals("Weather description should be 'clear sky'", "clear sky", response.weather?.get(0)?.description)
        assertEquals("Temperature should be 20.5", 20.5, response.main?.temp)
    }

    /**
     * Test Case 2: Edge case - Minimal weather response
     * Verifies that minimal JSON with null optional fields is parsed correctly
     */
    @Test
    fun testParseMinimalWeatherResponse() {
        val json = """{
            "weather": [{"id": 500, "main": "Rain", "description": "light rain", "icon": "10d"}],
            "name": "London"
        }"""

        val response = gson.fromJson(json, WeatherResponse::class.java)

        assertNotNull("Response should not be null", response)
        assertNull("Coordinates should be null", response.coord)
        assertNull("Main weather data should be null", response.main)
        assertNull("Wind data should be null", response.wind)
        assertEquals("Weather list should have one item", 1, response.weather?.size)
    }

    /**
     * Test Case 3: Edge case - Empty weather array
     * Verifies handling of empty weather list (no weather conditions)
     */
    @Test
    fun testParseWeatherResponseWithEmptyWeatherList() {
        val json = """{
            "weather": [],
            "name": "Paris",
            "main": {"temp": 15.0, "humidity": 75, "pressure": 1010}
        }"""

        val response = gson.fromJson(json, WeatherResponse::class.java)

        assertNotNull("Response should not be null", response)
        assertEquals("Weather list should be empty", 0, response.weather?.size)
        assertEquals("Temperature should be 15.0", 15.0, response.main?.temp)
    }

    /**
     * Test Case 4: Negative case - Null weather field
     * Verifies that null weather field doesn't cause parsing errors
     */
    @Test
    fun testParseWeatherResponseWithNullWeatherField() {
        val json = """{
            "weather": null,
            "name": "Tokyo",
            "main": {"temp": 25.0, "humidity": 80, "pressure": 1000}
        }"""

        val response = gson.fromJson(json, WeatherResponse::class.java)

        assertNotNull("Response should not be null", response)
        assertNull("Weather field should be null", response.weather)
        assertEquals("Temperature should be 25.0", 25.0, response.main?.temp)
    }

    /**
     * Test Case 5: Positive case - Multiple weather conditions
     * Verifies parsing of multiple weather conditions in array
     */
    @Test
    fun testParseWeatherResponseWithMultipleConditions() {
        val json = """{
            "weather": [
                {"id": 500, "main": "Rain", "description": "light rain", "icon": "10d"},
                {"id": 741, "main": "Fog", "description": "fog", "icon": "50d"}
            ],
            "name": "Amsterdam"
        }"""

        val response = gson.fromJson(json, WeatherResponse::class.java)

        assertEquals("Should have 2 weather conditions", 2, response.weather?.size)
        assertEquals("First condition should be Rain", "Rain", response.weather?.get(0)?.main)
        assertEquals("Second condition should be Fog", "Fog", response.weather?.get(1)?.main)
    }

    /**
     * Test Case 6: Edge case - Special characters in city name
     * Verifies that special characters and unicode are handled correctly
     */
    @Test
    fun testParseWeatherResponseWithSpecialCharactersInCityName() {
        val json = """{
            "weather": [{"id": 210, "main": "Thunderstorm", "description": "thunderstorm with light rain", "icon": "11d"}],
            "name": "São Paulo"
        }"""

        val response = gson.fromJson(json, WeatherResponse::class.java)

        assertEquals("City name should be São Paulo", "São Paulo", response.cityName)
    }

    /**
     * Test Case 7: Positive case - Wind data with optional gust
     * Verifies complete wind data parsing including optional gust field
     */
    @Test
    fun testParseWeatherResponseWithCompleteWindData() {
        val json = """{
            "weather": [{"id": 800, "main": "Clear", "description": "clear sky", "icon": "01d"}],
            "wind": {
                "speed": 8.5,
                "deg": 180,
                "gust": 12.5
            },
            "name": "NewYork"
        }"""

        val response = gson.fromJson(json, WeatherResponse::class.java)

        assertNotNull(response.wind)
        assertEquals("Wind speed should be 8.5", 8.5, response.wind?.speed)
        assertEquals("Wind direction should be 180", 180, response.wind?.deg)
        assertEquals("Wind gust should be 12.5", 12.5, response.wind?.gust)
    }
}

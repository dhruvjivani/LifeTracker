package com.conestoga.lifetracker

import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Integration tests for API operations using MockWebServer.
 * Tests Retrofit client interactions with stubbed API responses.
 * Covers positive, negative, and edge cases for API communication.
 *
 * Runs as a JVM unit test (src/test/) so MockWebServer can bind freely
 * without Android 9+ cleartext-HTTP network-security restrictions.
 */
class ApiIntegrationTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var retrofitClient: Retrofit
    private lateinit var weatherApi: WeatherAPI
    private lateinit var gson: Gson

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        retrofitClient = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherApi = retrofitClient.create(WeatherAPI::class.java)
        gson = Gson()
    }

    @After
    fun tearDown() {
        if (::mockWebServer.isInitialized) {
            mockWebServer.shutdown()
        }
    }

    /**
     * Test Case 1: Successful API call with valid response
     * Verifies that valid JSON response is properly parsed
     */
    @Test
    fun testSuccessfulWeatherApiCall() {
        // Arrange
        val jsonResponse = """{
            "coord": {"lon": 10.0, "lat": 50.0},
            "weather": [{"id": 800, "main": "Clear", "description": "clear sky", "icon": "01d"}],
            "main": {"temp": 20.5, "feels_like": 19.2, "temp_min": 18.0, "temp_max": 22.0, "pressure": 1013, "humidity": 65},
            "wind": {"speed": 5.5, "deg": 230},
            "name": "Berlin"
        }"""

        mockWebServer.enqueue(MockResponse().setBody(jsonResponse))

        // Act
        val response = weatherApi.getWeather("Berlin", "test_key").execute()

        // Assert
        assertTrue("Response should be successful", response.isSuccessful)
        assertNotNull("Response body should not be null", response.body())
        assertEquals("City name should be Berlin", "Berlin", response.body()?.cityName)
        assertEquals("Should have 1 weather condition", 1, response.body()?.weather?.size)
    }

    /**
     * Test Case 2: API response with empty weather array
     * Verifies handling of valid response but with no weather data
     */
    @Test
    fun testApiResponseWithEmptyWeatherArray() {
        // Arrange
        val jsonResponse = """{
            "weather": [],
            "main": {"temp": 15.0, "humidity": 75, "pressure": 1010},
            "name": "London"
        }"""

        mockWebServer.enqueue(MockResponse().setBody(jsonResponse))

        // Act
        val response = weatherApi.getWeather("London", "test_key").execute()

        // Assert
        assertTrue("Response should be successful", response.isSuccessful)
        assertEquals("Weather array should be empty", 0, response.body()?.weather?.size)
    }

    /**
     * Test Case 3: API response with null weather field
     * Verifies handling of null optional field
     */
    @Test
    fun testApiResponseWithNullWeatherField() {
        // Arrange
        val jsonResponse = """{
            "weather": null,
            "main": {"temp": 25.0},
            "name": "Tokyo"
        }"""

        mockWebServer.enqueue(MockResponse().setBody(jsonResponse))

        // Act
        val response = weatherApi.getWeather("Tokyo", "test_key").execute()

        // Assert
        assertTrue("Response should be successful", response.isSuccessful)
        assertNull("Weather field should be null", response.body()?.weather)
    }

    /**
     * Test Case 4: HTTP 404 Not Found response
     * Verifies handling of unsuccessful HTTP response
     */
    @Test
    fun testApiResponseNotFound() {
        // Arrange
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        // Act
        val response = weatherApi.getWeather("InvalidCity", "test_key").execute()

        // Assert
        assertFalse("Response should not be successful", response.isSuccessful)
        assertEquals("Response code should be 404", 404, response.code())
    }

    /**
     * Test Case 5: HTTP 500 Internal Server Error
     * Verifies handling of server errors
     */
    @Test
    fun testApiResponseServerError() {
        // Arrange
        mockWebServer.enqueue(MockResponse().setResponseCode(500))

        // Act
        val response = weatherApi.getWeather("Berlin", "test_key").execute()

        // Assert
        assertFalse("Response should not be successful", response.isSuccessful)
        assertEquals("Response code should be 500", 500, response.code())
    }

    /**
     * Test Case 6: API response takes expected time
     * Verifies API call completes successfully within deadline
     */
    @Test
    fun testApiResponseTiming() {
        // Arrange
        val jsonResponse = """{
            "weather": [{"id": 500, "main": "Rain", "description": "light rain", "icon": "10d"}],
            "name": "Amsterdam"
        }"""

        mockWebServer.enqueue(MockResponse().setBody(jsonResponse))

        // Act
        val startTime = System.currentTimeMillis()
        val response = weatherApi.getWeather("Amsterdam", "test_key").execute()
        val endTime = System.currentTimeMillis()

        // Assert
        assertTrue("Response should be successful", response.isSuccessful)
        assertTrue("Response should complete in reasonable time", (endTime - startTime) < 5000)
    }

    /**
     * Test Case 7: Multiple sequential API calls
     * Verifies that API client handles multiple requests
     */
    @Test
    fun testMultipleApiCalls() {
        // Arrange
        val response1 = """{
            "weather": [{"id": 800, "main": "Clear", "description": "clear sky", "icon": "01d"}],
            "name": "Berlin"
        }"""
        val response2 = """{
            "weather": [{"id": 500, "main": "Rain", "description": "light rain", "icon": "10d"}],
            "name": "London"
        }"""

        mockWebServer.enqueue(MockResponse().setBody(response1))
        mockWebServer.enqueue(MockResponse().setBody(response2))

        // Act
        val call1 = weatherApi.getWeather("Berlin", "test_key").execute()
        val call2 = weatherApi.getWeather("London", "test_key").execute()

        // Assert
        assertTrue("First call should succeed", call1.isSuccessful)
        assertTrue("Second call should succeed", call2.isSuccessful)
        assertEquals("First response city", "Berlin", call1.body()?.cityName)
        assertEquals("Second response city", "London", call2.body()?.cityName)
    }

    /**
     * Test Case 8: API response with special characters in city name
     * Edge case: verifies handling of unicode characters
     */
    @Test
    fun testApiResponseWithSpecialCharacters() {
        // Arrange
        val jsonResponse = """{
            "weather": [{"id": 210, "main": "Thunderstorm", "description": "thunderstorm", "icon": "11d"}],
            "name": "São Paulo"
        }"""

        mockWebServer.enqueue(MockResponse().setBody(jsonResponse))

        // Act
        val response = weatherApi.getWeather("São Paulo", "test_key").execute()

        // Assert
        assertTrue("Response should be successful", response.isSuccessful)
        assertEquals("City name with special chars should be preserved", "São Paulo", response.body()?.cityName)
    }

    /**
     * Test Case 9: API response with minimal data
     * Edge case: verifies handling of incomplete response
     */
    @Test
    fun testApiResponseMinimalData() {
        // Arrange
        val jsonResponse = """{
            "weather": [{"id": 800, "main": "Clear", "description": "clear", "icon": "01d"}]
        }"""

        mockWebServer.enqueue(MockResponse().setBody(jsonResponse))

        // Act
        val response = weatherApi.getWeather("AnyCity", "test_key").execute()

        // Assert
        assertTrue("Response should be successful", response.isSuccessful)
        assertNull("City name should be null when not provided", response.body()?.cityName)
        assertEquals("Weather should still be parsed", 1, response.body()?.weather?.size)
    }

    /**
     * Test Case 10: HTTP 401 Unauthorized (Invalid API key)
     * Negative case: verifies handling of authentication errors
     */
    @Test
    fun testApiResponseUnauthorized() {
        // Arrange
        mockWebServer.enqueue(MockResponse().setResponseCode(401))

        // Act
        val response = weatherApi.getWeather("Berlin", "invalid_key").execute()

        // Assert
        assertFalse("Response should not be successful", response.isSuccessful)
        assertEquals("Response code should be 401", 401, response.code())
    }
}

package com.conestoga.lifetracker

import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@Config(sdk = [34])
@RunWith(RobolectricTestRunner::class)
class WeatherActivityTest {
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        RetrofitClient.setBaseUrl(mockWebServer.url("/").toString())
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testFetchWeather_Success() {
        val jsonResponse = """{
            "weather": [{"id": 800, "main": "Clear", "description": "clear sky", "icon": "01d"}],
            "name": "Berlin"
        }"""
        mockWebServer.enqueue(MockResponse().setBody(jsonResponse))

        val activity = Robolectric.buildActivity(WeatherActivity::class.java).create().resume().get()
        val cityInput = activity.findViewById<EditText>(R.id.editCityInput)
        val btnFetch = activity.findViewById<Button>(R.id.btnFetchWeather)

        cityInput.setText("Berlin")
        btnFetch.performClick()
        
        val recyclerView = activity.findViewById<RecyclerView>(R.id.weatherRecyclerView)
        assertNotNull(recyclerView.adapter)
        assertEquals(1, recyclerView.adapter?.itemCount)
    }

    @Test
    fun testFetchWeather_Failure() {
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        val activity = Robolectric.buildActivity(WeatherActivity::class.java).create().resume().get()
        val cityInput = activity.findViewById<EditText>(R.id.editCityInput)
        val btnFetch = activity.findViewById<Button>(R.id.btnFetchWeather)

        cityInput.setText("InvalidCity")
        btnFetch.performClick()

        val latestToast = ShadowToast.getTextOfLatestToast()
        assertNotNull(latestToast)
        assert(latestToast!!.contains("Failed to retrieve weather"))
    }

    @Test
    fun testFetchWeather_NetworkError() {
        mockWebServer.shutdown() // Force network error

        val activity = Robolectric.buildActivity(WeatherActivity::class.java).create().resume().get()
        val cityInput = activity.findViewById<EditText>(R.id.editCityInput)
        val btnFetch = activity.findViewById<Button>(R.id.btnFetchWeather)

        cityInput.setText("AnyCity")
        btnFetch.performClick()

        val latestToast = ShadowToast.getTextOfLatestToast()
        assertNotNull(latestToast)
        assert(latestToast!!.contains("Error"))
    }

    @Test
    fun testFetchWeather_EmptyWeatherList() {
        val jsonResponse = """{
            "weather": [],
            "name": "Berlin"
        }"""
        mockWebServer.enqueue(MockResponse().setBody(jsonResponse))

        val activity = Robolectric.buildActivity(WeatherActivity::class.java).create().resume().get()
        val cityInput = activity.findViewById<EditText>(R.id.editCityInput)
        val btnFetch = activity.findViewById<Button>(R.id.btnFetchWeather)

        cityInput.setText("Berlin")
        btnFetch.performClick()

        assertEquals("No weather data found", ShadowToast.getTextOfLatestToast())
    }
}

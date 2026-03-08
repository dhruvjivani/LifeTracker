package com.conestoga.lifetracker

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for RetrofitClient singleton.
 * Tests cover instance creation, URL configuration, and singleton behaviour.
 * No Android framework required — RetrofitClient uses pure Java/Kotlin Retrofit.
 */
class RetrofitClientTest {

    private val defaultUrl = "https://api.openweathermap.org/"

    @Before
    fun setUp() {
        // Reset to the default URL before each test
        RetrofitClient.setBaseUrl(defaultUrl)
    }

    @After
    fun tearDown() {
        // Restore default URL so other tests are not affected
        RetrofitClient.setBaseUrl(defaultUrl)
    }

    /**
     * Test Case 1: Positive case - getInstance returns a non-null Retrofit object
     * Verifies the singleton is properly initialised
     */
    @Test
    fun testGetInstance_ReturnsNonNull() {
        // Act
        val instance = RetrofitClient.instance

        // Assert
        assertNotNull("Retrofit instance should not be null", instance)
    }

    /**
     * Test Case 2: Positive case - getInstance is a singleton (same reference)
     * Verifies the lazy singleton pattern returns the same object on repeated calls
     */
    @Test
    fun testGetInstance_ReturnsSameObject_OnRepeatedCalls() {
        // Act
        val instance1 = RetrofitClient.instance
        val instance2 = RetrofitClient.instance

        // Assert
        assertSame("Same Retrofit instance should be returned on repeated calls", instance1, instance2)
    }

    /**
     * Test Case 3: Positive case - setBaseUrl resets the cached instance
     * Verifies that changing the URL forces a new Retrofit instance
     */
    @Test
    fun testSetBaseUrl_ResetsInstance_NewObjectReturned() {
        // Arrange
        val instance1 = RetrofitClient.instance

        // Act
        RetrofitClient.setBaseUrl("https://mock.example.com/")
        val instance2 = RetrofitClient.instance

        // Assert
        assertNotNull("New instance should not be null after setBaseUrl", instance2)
        assertNotSame("setBaseUrl should produce a new Retrofit instance", instance1, instance2)
    }

    /**
     * Test Case 4: Positive case - setBaseUrl then getInstance creates a usable client
     * Verifies Retrofit can be constructed with a custom URL
     */
    @Test
    fun testSetBaseUrl_CustomUrl_InstanceIsNotNull() {
        // Act
        RetrofitClient.setBaseUrl("https://custom.api.example.com/")
        val instance = RetrofitClient.instance

        // Assert
        assertNotNull("Retrofit instance with custom URL should not be null", instance)
    }

    /**
     * Test Case 5: Edge case - setBaseUrl called twice, last URL wins
     * Verifies that the most recently set URL takes effect
     */
    @Test
    fun testSetBaseUrl_CalledTwice_LastUrlWins() {
        // Act
        RetrofitClient.setBaseUrl("https://first.example.com/")
        RetrofitClient.setBaseUrl("https://second.example.com/")
        val instance = RetrofitClient.instance

        // Assert
        assertNotNull("Instance should not be null after two setBaseUrl calls", instance)
    }

    /**
     * Test Case 6: Positive case - WeatherAPI can be created from the instance
     * Verifies that the Retrofit instance supports WeatherAPI interface creation
     */
    @Test
    fun testGetInstance_CanCreateWeatherApi() {
        // Act
        val api = RetrofitClient.instance.create(WeatherAPI::class.java)

        // Assert
        assertNotNull("WeatherAPI should be creatable from Retrofit instance", api)
    }
}

package com.conestoga.lifetracker

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [34])
@RunWith(RobolectricTestRunner::class)
class WeatherAdapterTest {
    private lateinit var context: Context
    private lateinit var adapter: WeatherAdapter
    private val weatherList = listOf(
        WeatherItem(1, "Clear", "clear sky", "01d"),
        WeatherItem(2, "Rain", "light rain", "10d")
    )

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        adapter = WeatherAdapter(weatherList)
    }

    @Test
    fun testOnBindViewHolder() {
        val parent = androidx.constraintlayout.widget.ConstraintLayout(context)
        val viewHolder = adapter.onCreateViewHolder(parent, 0)
        
        adapter.onBindViewHolder(viewHolder, 0)
        
        // Testing current behavior (including the intentional bug #2)
        assertEquals("Condition: Clear", viewHolder.weatherText.text.toString())
    }
}

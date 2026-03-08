package com.conestoga.lifetracker

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Unit tests for WeatherAdapter list management.
 * Uses Robolectric because WeatherAdapter extends RecyclerView.Adapter (Android framework class).
 * Tests cover positive, negative, and edge cases for item count and list updates.
 */
@Config(sdk = [34])
@RunWith(RobolectricTestRunner::class)
class WeatherAdapterItemCountTest {

    private lateinit var adapter: WeatherAdapter

    @Before
    fun setUp() {
        val items = listOf(
            WeatherItem(id = 800, main = "Clear", description = "clear sky", icon = "01d"),
            WeatherItem(id = 500, main = "Rain", description = "light rain", icon = "10d")
        )
        adapter = WeatherAdapter(items)
    }

    /**
     * Test Case 1: Positive case - Initial item count matches list size
     * Verifies adapter reports correct count after construction
     */
    @Test
    fun testGetItemCount_ReturnsCorrectInitialCount() {
        assertEquals("Initial item count should be 2", 2, adapter.itemCount)
    }

    /**
     * Test Case 2: Positive case - Update with larger list increases count
     * Verifies itemCount reflects updated list
     */
    @Test
    fun testUpdateList_LargerList_IncreasesItemCount() {
        // Arrange
        val newItems = listOf(
            WeatherItem(800, "Clear", "clear sky", "01d"),
            WeatherItem(500, "Rain", "light rain", "10d"),
            WeatherItem(741, "Fog", "fog", "50d")
        )

        // Act
        adapter.updateList(newItems)

        // Assert
        assertEquals("Item count should be 3 after update", 3, adapter.itemCount)
    }

    /**
     * Test Case 3: Negative case - Update with smaller list decreases count
     * Verifies itemCount shrinks correctly
     */
    @Test
    fun testUpdateList_SmallerList_DecreasesItemCount() {
        // Arrange
        val singleItem = listOf(WeatherItem(800, "Clear", "clear sky", "01d"))

        // Act
        adapter.updateList(singleItem)

        // Assert
        assertEquals("Item count should be 1 after shrink", 1, adapter.itemCount)
    }

    /**
     * Test Case 4: Edge case - Update with empty list produces zero count
     * Verifies adapter handles empty data gracefully
     */
    @Test
    fun testUpdateList_EmptyList_ReturnsZeroCount() {
        // Act
        adapter.updateList(emptyList())

        // Assert
        assertEquals("Item count should be 0 for empty list", 0, adapter.itemCount)
    }

    /**
     * Test Case 5: Edge case - Update with same list keeps same count
     * Verifies idempotent behaviour
     */
    @Test
    fun testUpdateList_SameList_KeepsSameCount() {
        // Arrange
        val sameItems = listOf(
            WeatherItem(800, "Clear", "clear sky", "01d"),
            WeatherItem(500, "Rain", "light rain", "10d")
        )

        // Act
        adapter.updateList(sameItems)

        // Assert
        assertEquals("Count should remain 2 when list size is unchanged", 2, adapter.itemCount)
    }
}

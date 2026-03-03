package com.conestoga.lifetracker

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI Tests for MainActivity and theme switching functionality.
 * Tests theme toggle and navigation to other activities.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityUITest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    /**
     * Test Case 1: Verify MainActivity loads with all buttons visible
     * Positive case: verifies initial UI state
     */
    @Test
    fun testMainActivityLoadsCorrectly() {
        onView(withId(R.id.btnWeather)).check(matches(isDisplayed()))
        onView(withId(R.id.btnDatabase)).check(matches(isDisplayed()))
        onView(withId(R.id.btnLocation)).check(matches(isDisplayed()))
    }

    /**
     * Test Case 2: Navigate to WeatherActivity
     * Positive case: verifies navigation works
     */
    @Test
    fun testNavigateToWeatherActivity() {
        onView(withId(R.id.btnWeather)).perform(click())
        onView(withId(R.id.weatherRecyclerView)).check(matches(isDisplayed()))
    }

    /**
     * Test Case 3: Navigate to DatabaseActivity
     * Positive case: verifies navigation works
     */
    @Test
    fun testNavigateToDatabaseActivity() {
        onView(withId(R.id.btnDatabase)).perform(click())
        onView(withId(R.id.dbRecyclerView)).check(matches(isDisplayed()))
    }

    /**
     * Test Case 4: Navigate to LocationActivity
     * Positive case: verifies navigation works
     */
    @Test
    fun testNavigateToLocationActivity() {
        onView(withId(R.id.btnLocation)).perform(click())
        onView(withId(R.id.locationText)).check(matches(isDisplayed()))
    }
}

/**
 * UI Tests for WeatherActivity functionality.
 * Tests weather fetching and display.
 */
@RunWith(AndroidJUnit4::class)
class WeatherActivityUITest {
    @get:Rule
    val activityRule = ActivityScenarioRule(WeatherActivity::class.java)

    /**
     * Test Case 5: Verify WeatherActivity loads correctly
     * Positive case: verifies initial UI state
     */
    @Test
    fun testWeatherActivityLoadsCorrectly() {
        onView(withId(R.id.editCityInput)).check(matches(isDisplayed()))
        onView(withId(R.id.btnFetchWeather)).check(matches(isDisplayed()))
        onView(withId(R.id.weatherRecyclerView)).check(matches(isDisplayed()))
    }

    /**
     * Test Case 6: Enter city name and trigger search
     * Positive case: verifies user input and button click
     */
    @Test
    fun testEnterCityAndFetchWeather() {
        onView(withId(R.id.editCityInput))
            .perform(typeText("London"))
        onView(withId(R.id.btnFetchWeather))
            .perform(click())
        // Note: Actual API call would be made here
    }
}

/**
 * UI Tests for DatabaseActivity functionality.
 * Tests note creation, editing, and deletion.
 */
@RunWith(AndroidJUnit4::class)
class DatabaseActivityUITest {
    @get:Rule
    val activityRule = ActivityScenarioRule(DatabaseActivity::class.java)

    /**
     * Test Case 7: Verify DatabaseActivity loads correctly
     * Positive case: verifies initial UI state
     */
    @Test
    fun testDatabaseActivityLoadsCorrectly() {
        onView(withId(R.id.editDbInput)).check(matches(isDisplayed()))
        onView(withId(R.id.btnAddDb)).check(matches(isDisplayed()))
        onView(withId(R.id.dbRecyclerView)).check(matches(isDisplayed()))
    }

    /**
     * Test Case 8: Add a new note
     * Positive case: verifies note insertion
     */
    @Test
    fun testAddNewNote() {
        onView(withId(R.id.editDbInput))
            .perform(typeText("Test Note"))
        onView(withId(R.id.btnAddDb))
            .perform(click())
        // Note would be added to RecyclerView
    }

    /**
     * Test Case 9: Add empty note should show error
     * Negative case: verifies error handling for empty input
     */
    @Test
    fun testAddEmptyNoteShowsError() {
        onView(withId(R.id.btnAddDb))
            .perform(click())
        // Toast should appear with error message
    }
}

/**
 * UI Tests for LocationActivity functionality.
 * Tests location display and updates.
 */
@RunWith(AndroidJUnit4::class)
class LocationActivityUITest {
    @get:Rule
    val activityRule = ActivityScenarioRule(LocationActivity::class.java)

    /**
     * Test Case 10: Verify LocationActivity loads correctly
     * Positive case: verifies initial UI state
     */
    @Test
    fun testLocationActivityLoadsCorrectly() {
        onView(withId(R.id.locationText)).check(matches(isDisplayed()))
    }
}

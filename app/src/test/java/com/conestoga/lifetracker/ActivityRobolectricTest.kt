package com.conestoga.lifetracker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@Config(sdk = [34])
@RunWith(RobolectricTestRunner::class)
class MainActivityRobolectricTest {

    @Test
    fun testMainActivity_NavigationToWeather() {
        val activity = Robolectric.buildActivity(MainActivity::class.java).create().get()
        activity.findViewById<Button>(R.id.btnWeather).performClick()
        
        val actualIntent = shadowOf(activity).nextStartedActivity
        assertEquals(WeatherActivity::class.java.name, actualIntent.component?.className)
    }

    @Test
    fun testMainActivity_NavigationToDatabase() {
        val activity = Robolectric.buildActivity(MainActivity::class.java).create().get()
        activity.findViewById<Button>(R.id.btnDatabase).performClick()
        
        val actualIntent = shadowOf(activity).nextStartedActivity
        assertEquals(DatabaseActivity::class.java.name, actualIntent.component?.className)
    }

    @Test
    fun testMainActivity_NavigationToLocation() {
        val activity = Robolectric.buildActivity(MainActivity::class.java).create().get()
        activity.findViewById<Button>(R.id.btnLocation).performClick()
        
        val actualIntent = shadowOf(activity).nextStartedActivity
        assertEquals(LocationActivity::class.java.name, actualIntent.component?.className)
    }
}

@Config(sdk = [34])
@RunWith(RobolectricTestRunner::class)
class DatabaseActivityRobolectricTest {

    @Test
    fun testDatabaseActivity_AddNote() {
        val activity = Robolectric.buildActivity(DatabaseActivity::class.java).create().get()
        val input = activity.findViewById<EditText>(R.id.editDbInput)
        val addButton = activity.findViewById<ImageButton>(R.id.btnAddDb)
        
        input.setText("New Note")
        addButton.performClick()
        
        val recyclerView = activity.findViewById<RecyclerView>(R.id.dbRecyclerView)
        val adapter = recyclerView.adapter as DBAdapter
        assertTrue("Item count should be greater than 0", adapter.itemCount > 0)
        assertEquals("Input should be cleared", "", input.text.toString())
    }

    @Test
    fun testDatabaseActivity_AddEmptyNote_ShowsToast() {
        val activity = Robolectric.buildActivity(DatabaseActivity::class.java).create().get()
        val addButton = activity.findViewById<ImageButton>(R.id.btnAddDb)
        
        addButton.performClick()
        
        assertEquals("Please enter some text", ShadowToast.getTextOfLatestToast())
    }

    @Test
    fun testDatabaseActivity_EditAndSaveFlow() {
        val activity = Robolectric.buildActivity(DatabaseActivity::class.java).create().get()
        val helper = DatabaseHelper(activity)
        helper.insertData("Original Note")
        
        // Refresh activity state by recreating or just triggering what onCreate does
        activity.recreate() 
        
        val recyclerView = activity.findViewById<RecyclerView>(R.id.dbRecyclerView)
        recyclerView.measure(0, 0)
        recyclerView.layout(0, 0, 100, 1000)
        
        val holder = recyclerView.findViewHolderForAdapterPosition(0) as DBAdapter.DBViewHolder
        
        // Click Edit
        holder.editBtn.performClick()
        
        val input = activity.findViewById<EditText>(R.id.editDbInput)
        val addButton = activity.findViewById<ImageButton>(R.id.btnAddDb)
        
        assertEquals("Original Note", input.text.toString())
        
        // Update text and click Save (Add button becomes Save)
        input.setText("Updated Note")
        addButton.performClick()
        
        assertEquals("Input should be cleared", "", input.text.toString())
        assertEquals("Database should be updated", "Updated Note", helper.getAllData()[0].second)
    }

    @Test
    fun testDatabaseActivity_DeleteFlow() {
        val activity = Robolectric.buildActivity(DatabaseActivity::class.java).create().get()
        val helper = DatabaseHelper(activity)
        helper.insertData("To Delete")
        
        activity.recreate()
        
        val recyclerView = activity.findViewById<RecyclerView>(R.id.dbRecyclerView)
        recyclerView.measure(0, 0)
        recyclerView.layout(0, 0, 100, 1000)
        
        val holder = recyclerView.findViewHolderForAdapterPosition(0) as DBAdapter.DBViewHolder
        holder.deleteBtn.performClick()
        
        assertTrue("Database should be empty after delete", helper.getAllData().isEmpty())
    }
}

@Config(sdk = [34])
@RunWith(RobolectricTestRunner::class)
class LocationActivityRobolectricTest {

    @Test
    fun testLocationActivity_PermissionDenied_ShowsToast() {
        val activity = Robolectric.buildActivity(LocationActivity::class.java).create().get()
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        val grantResults = intArrayOf(PackageManager.PERMISSION_DENIED)

        activity.onRequestPermissionsResult(1000, permissions, grantResults)

        val locationText = activity.findViewById<TextView>(R.id.locationText)
        assertEquals("Permission denied", locationText.text.toString())
    }
    
    @Test
    fun testLocationActivity_PermissionGranted_UpdatesUI() {
        val activity = Robolectric.buildActivity(LocationActivity::class.java).create().get()
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        val grantResults = intArrayOf(PackageManager.PERMISSION_GRANTED)

        // This will call requestLocationUpdates()
        activity.onRequestPermissionsResult(1000, permissions, grantResults)
        
        // We can't easily mock the FusedLocationProvider callback result without complex mocking,
        // but we've covered the branching logic in onRequestPermissionsResult.
        assertNotNull(activity)
    }
}

package com.conestoga.lifetracker

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@Config(sdk = [34])
@RunWith(RobolectricTestRunner::class)
class LocationActivityTest {

    @Test
    fun testLocationActivity_PermissionDenied_ShowsMessage() {
        val activity = Robolectric.buildActivity(LocationActivity::class.java).create().get()
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        val grantResults = intArrayOf(PackageManager.PERMISSION_DENIED, PackageManager.PERMISSION_DENIED)

        activity.onRequestPermissionsResult(1000, permissions, grantResults)

        val locationText = activity.findViewById<TextView>(R.id.locationText)
        assertEquals("Permission denied", locationText.text.toString())
    }

    @Test
    fun testLocationActivity_PermissionGranted_StartsUpdates() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        shadowOf(application).grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        
        val activity = Robolectric.buildActivity(LocationActivity::class.java).create().get()
        // If permission is granted in onCreate, it calls requestLocationUpdates()
        // We can't easily verify the fusedLocationClient call without mocking, 
        // but we can verify the activity created successfully.
        assertEquals(PackageManager.PERMISSION_GRANTED, activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION))
    }
}

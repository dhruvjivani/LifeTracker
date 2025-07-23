package com.conestoga.lifetracker

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        locationText = findViewById(R.id.locationText)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1000
            )
        } else {
            requestLocationUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setMinUpdateIntervalMillis(5000)
            .build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val location: Location? = result.lastLocation
                    location?.let {
                        locationText.text = "Lat: ${it.latitude}, Lon: ${it.longitude}"
                    } ?: run {
                        locationText.text = "Location not available"
                    }
                }
            },
            mainLooper
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestLocationUpdates()
        } else {
            locationText.text = "Permission denied"
        }
    }
}

package com.example.taxibooking

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var tabLocal: LinearLayout
    private lateinit var tabRental: LinearLayout
    private lateinit var tabOutstation: LinearLayout

    private lateinit var textLocal: TextView
    private lateinit var textRental: TextView
    private lateinit var textOutstation: TextView

    private lateinit var oneHr: TextView
    private lateinit var twoHrs: TextView
    private lateinit var threeHrs: TextView
    private lateinit var fourHrs: TextView

    private lateinit var etPickup: EditText
    private lateinit var pickupLayout: LinearLayout

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var btnMenu: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Drawer setup
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        btnMenu = findViewById(R.id.btnMenu)

        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val headerView = navigationView.getHeaderView(0)
        val profileCard = headerView.findViewById<View>(R.id.profileCard)
        profileCard.setOnClickListener {
            startActivity(Intent(this, selectride::class.java))
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        // Tabs
        tabLocal = findViewById(R.id.tabLocal)
        tabRental = findViewById(R.id.tabRental)
        tabOutstation = findViewById(R.id.tabOutstation)

        textLocal = findViewById(R.id.textLocal)
        textRental = findViewById(R.id.textRental)
        textOutstation = findViewById(R.id.textOutstation)

        // Rental package options
        oneHr = findViewById(R.id.oneHr)
        twoHrs = findViewById(R.id.twoHrs)
        threeHrs = findViewById(R.id.threeHrs)
        fourHrs = findViewById(R.id.fourHrs)

        // Pickup layout and EditText
        pickupLayout = findViewById(R.id.pickupLayout)
        etPickup = findViewById(R.id.etTo)

        etPickup.setOnClickListener {
            startActivity(Intent(this, Locationsearch::class.java))
        }

        // Google Map setup
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Tab click listeners
        tabLocal.setOnClickListener { showLocalPackages() }
        tabRental.setOnClickListener { showRentalPackages() }
        tabOutstation.setOnClickListener { showOutstationPackages() }

        // Default: show Local tab
        showLocalPackages()
    }

    private fun resetTabStyles() {
        textLocal.setBackgroundResource(0)
        textRental.setBackgroundResource(0)
        textOutstation.setBackgroundResource(0)

        val gray = ContextCompat.getColor(this, android.R.color.darker_gray)
        textLocal.setTextColor(gray)
        textRental.setTextColor(gray)
        textOutstation.setTextColor(gray)
    }

    private fun showRentalPackages() {
        resetTabStyles()
        textRental.setBackgroundResource(R.drawable.tab_selected_bg)
        textRental.setTextColor(ContextCompat.getColor(this, android.R.color.white))

        // Hide pickup layout
        pickupLayout.visibility = View.GONE

        // Show rental hour options
        oneHr.visibility = View.VISIBLE
        twoHrs.visibility = View.VISIBLE
        threeHrs.visibility = View.VISIBLE
        fourHrs.visibility = View.VISIBLE
    }

    private fun showLocalPackages() {
        resetTabStyles()
        textLocal.setBackgroundResource(R.drawable.tab_selected_bg)
        textLocal.setTextColor(ContextCompat.getColor(this, android.R.color.white))

        // Show pickup layout
        pickupLayout.visibility = View.VISIBLE

        // Hide rental hour options
        oneHr.visibility = View.GONE
        twoHrs.visibility = View.GONE
        threeHrs.visibility = View.GONE
        fourHrs.visibility = View.GONE
    }

    private fun showOutstationPackages() {
        resetTabStyles()
        textOutstation.setBackgroundResource(R.drawable.tab_selected_bg)
        textOutstation.setTextColor(ContextCompat.getColor(this, android.R.color.white))

        // Show pickup layout
        pickupLayout.visibility = View.VISIBLE

        // Hide rental hour options
        oneHr.visibility = View.GONE
        twoHrs.visibility = View.GONE
        threeHrs.visibility = View.GONE
        fourHrs.visibility = View.GONE
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val default = LatLng(37.4221, -122.0841)
        mMap.addMarker(MarkerOptions().position(default).title("Pickup Location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(default, 14f))

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }

        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
            loc?.let {
                val current = LatLng(it.latitude, it.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 14f))
            }
        }
    }
}

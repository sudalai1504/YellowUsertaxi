package com.example.taxibooking

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog

class selectride : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var confirmButton: Button
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_selectride)

        // Confirm button
        confirmButton = findViewById(R.id.btnConfirmRide)
        confirmButton.setOnClickListener {
            showBookingDialog()
        }

        // ---------- Map Section ----------
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragments) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // ---------- Coupon Section ----------
        val couponLayout = findViewById<LinearLayout>(R.id.copan)
        couponLayout.setOnClickListener {
            showCouponBottomSheet()
        }

        // ---------- Profile Section ----------
        val profileLayout = findViewById<LinearLayout>(R.id.profile)
        profileLayout.setOnClickListener {
            showBookingForBottomSheet()
        }

        // ---------- Payment Section ----------
        val Cash = findViewById<LinearLayout>(R.id.cash)
        Cash.setOnClickListener {
            showCashForBottomSheet()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
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

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                mMap.addMarker(MarkerOptions().position(currentLatLng).title("Pickup Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            } ?: run {
                val newYork = LatLng(40.748817, -73.985428)
                mMap.addMarker(MarkerOptions().position(newYork).title("Default Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newYork, 14f))
            }
        }
    }

    // ---------- Booking Success Dialog ----------
    private fun showBookingDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_booking_success, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val btnDone = dialogView.findViewById<Button>(R.id.btnDone)
        btnDone.setOnClickListener {
            dialog.dismiss()
            navigateToMain()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        // Auto close after 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            if (dialog.isShowing) {
                dialog.dismiss()
                navigateToMain()
            }
        }, 3000)
    }

    private fun navigateToMain() {
        val intent = Intent(this, ride_booking::class.java)
        startActivity(intent)
        finish()
    }

    // ---------- Coupon BottomSheet ----------
    private fun showCouponBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_coupon, null)
        val edtCoupon = dialogView.findViewById<EditText>(R.id.edtCoupon)
        val btnReset = dialogView.findViewById<Button>(R.id.btnReset)
        val btnApply = dialogView.findViewById<Button>(R.id.btnApply)

        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(dialogView)

        btnReset.setOnClickListener {
            edtCoupon.setText("")
        }

        btnApply.setOnClickListener {
            val coupon = edtCoupon.text.toString().trim()
            if (coupon.isNotEmpty()) {
                Toast.makeText(this, "Coupon Applied: $coupon", Toast.LENGTH_SHORT).show()
                bottomSheetDialog.dismiss()
            } else {
                Toast.makeText(this, "Enter a coupon!", Toast.LENGTH_SHORT).show()
            }
        }

        bottomSheetDialog.show()
    }

    // ---------- Booking For BottomSheet ----------
    private fun showBookingForBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_booking_for, null)
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroupBooking)
        val btnDone = dialogView.findViewById<Button>(R.id.btnDoneBookingFor)

        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(dialogView)

        btnDone.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId != -1) {
                val selectedText =
                    dialogView.findViewById<RadioButton>(selectedId)?.text.toString()
                Toast.makeText(this, "Selected: $selectedText", Toast.LENGTH_SHORT).show()
            }
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    // ---------- Payment Methods BottomSheet ----------
    private fun showCashForBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_payment_methods, null)

        val radioFlutter = dialogView.findViewById<RadioButton>(R.id.radioFlutter)
        val radioRazor = dialogView.findViewById<RadioButton>(R.id.radioRazor)
        val radioCash = dialogView.findViewById<RadioButton>(R.id.radioCash)
        val btnDone = dialogView.findViewById<Button>(R.id.btnDonePayment)

        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(dialogView)

        fun selectOnly(selected: RadioButton) {
            radioFlutter.isChecked = false
            radioRazor.isChecked = false
            radioCash.isChecked = false
            selected.isChecked = true
        }

        radioFlutter.setOnClickListener { selectOnly(radioFlutter) }
        radioRazor.setOnClickListener { selectOnly(radioRazor) }
        radioCash.setOnClickListener { selectOnly(radioCash) }

        btnDone.setOnClickListener {
            val selectedMethod = when {
                radioFlutter.isChecked -> "Flutterwave"
                radioRazor.isChecked -> "Razorpay"
                radioCash.isChecked -> "Cash"
                else -> null
            }

            if (selectedMethod != null) {
                Toast.makeText(this, "Selected: $selectedMethod", Toast.LENGTH_SHORT).show()
                bottomSheetDialog.dismiss()
            } else {
                Toast.makeText(this, "Please select a payment method!", Toast.LENGTH_SHORT).show()
            }
        }

        bottomSheetDialog.show()
    }
}

package com.example.taxibooking.ForgotPassword

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taxibooking.R

class forgotpassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgotpassword)

        val Sendotp = findViewById<Button>(R.id.sendOtpButton)

        Sendotp.setOnClickListener {
            val intent = Intent(this, forgotpasswordotp::class.java)
            startActivity(intent)
        }
    }
}

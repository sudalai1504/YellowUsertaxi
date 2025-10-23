package com.example.taxibooking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.taxibooking.ForgotPassword.forgotpassword

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val btnGetStarted = findViewById<Button>(R.id.btnSignUp)
        val btncreatenewaccount = findViewById<TextView>(R.id.tvcreatenewaccount)

        btncreatenewaccount.setOnClickListener {
            val intent = Intent(this, sign_up::class.java)
            startActivity(intent)
            finish()
        }

        val forgotPassword = findViewById<TextView>(R.id.ForgotPassword)

        forgotPassword.setOnClickListener {
            val intent = Intent(this, forgotpassword::class.java)
            startActivity(intent)
        }


        btnGetStarted.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 🔙 Back button press handling
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Example → splashscreen activity க்கு போகணும்னா
                val intent = Intent(this@Login, splashscreen::class.java)
                startActivity(intent)
                finish()
            }
        })
    }
}

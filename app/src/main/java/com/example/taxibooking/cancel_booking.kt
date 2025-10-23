package com.example.taxibooking

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class cancel_booking : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cancel_booking)

        // Initialize views
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupReasons)
        val etOtherReason = findViewById<EditText>(R.id.etOtherReason)
        val btnSend = findViewById<Button>(R.id.btnSend)

        // Handle radio button selection
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.radioOther) {
                etOtherReason.visibility = View.VISIBLE
            } else {
                etOtherReason.visibility = View.GONE
            }
        }

        // Handle Send button click
        btnSend.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId

            if (selectedId == -1) {
                Toast.makeText(this, "Please select a reason", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedRadio = findViewById<RadioButton>(selectedId)
            var reason = selectedRadio.text.toString()

            if (selectedId == R.id.radioOther) {
                val otherText = etOtherReason.text.toString().trim()
                if (otherText.isEmpty()) {
                    Toast.makeText(this, "Please type your reason", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                reason = otherText
            }

            Toast.makeText(this, "Reason sent: $reason", Toast.LENGTH_LONG).show()

            // TODO: Add API call or navigation logic here if needed
            finish()
        }
    }
}

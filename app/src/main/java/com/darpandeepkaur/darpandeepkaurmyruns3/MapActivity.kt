package com.darpandeepkaur.darpandeepkaurmyruns3

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MapActivity : AppCompatActivity() {
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)

        saveButton.setOnClickListener(){
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
        }

        cancelButton.setOnClickListener(){
            finish()
        }
    }
}
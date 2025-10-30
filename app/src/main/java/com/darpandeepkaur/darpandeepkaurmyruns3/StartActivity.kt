package com.darpandeepkaur.darpandeepkaurmyruns3

import android.app.AlertDialog
import android.icu.text.DateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.darpandeepkaur.darpandeepkaurmyruns3.database.ExerciseDatabase
import com.darpandeepkaur.darpandeepkaurmyruns3.database.ExerciseEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartActivity : AppCompatActivity() {

    private lateinit var mylistview: ListView
    private var date = Calendar.getInstance()
    private var time = Calendar.getInstance()
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    var duration: Int? = null
    var distance: Int? = null
    var calories: Int? = null
    var heartRate: Int? = null
    var comments: String? = null

    private val OPTIONS = arrayOf(
        "Date", "Time", "Duration", "Distance",
        "Calories", "Heart Rate", "Comments"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        mylistview = findViewById(R.id.myListView)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, OPTIONS)
        mylistview.adapter = arrayAdapter

        mylistview.setOnItemClickListener { _, _, position, id ->
            when (position) {
                0 -> showDatePicker { year, month, day ->
                    date.set(year, month, day)
                }

                1 -> showTimePicker { hour, minute ->
                    time.set(hour, minute)
                }
                2 -> showInputDialog("Duration","Enter duration", DialogFrag.INT_DIALOG) {
                        value -> duration = value.toIntOrNull()
                }
                3 -> showInputDialog("Distance", "Enter distance", DialogFrag.INT_DIALOG) {
                        value -> distance = value.toIntOrNull()
                }
                4 -> showInputDialog("Calories", "Enter calories", DialogFrag.INT_DIALOG) {
                        value -> calories = value.toIntOrNull()
                }
                5 -> showInputDialog("Heart Rate", "Enter Heart Rate", DialogFrag.INT_DIALOG) {
                        value -> heartRate = value.toIntOrNull()
                }
                6 -> showInputDialog("Comments", "Enter Comments", DialogFrag.TEXT_DIALOG) {
                        value -> comments = value
                }
                else -> Toast.makeText(this, "Clicked: ${OPTIONS[position]}", Toast.LENGTH_SHORT).show()
            }

        }

        saveButton.setOnClickListener {
            // Create a background coroutine
            lifecycleScope.launch(Dispatchers.IO) {
                val dao = ExerciseDatabase.getInstance(this@StartActivity).exerciseDao()

                val entry = ExerciseEntry(
                    inputType = 0,
                    activityType = 0,
                    dateTime = System.currentTimeMillis(),
                    duration = duration?.toDouble() ?: 0.0,
                    distance = distance?.toDouble() ?: 0.0,
                    avgPace = null,
                    avgSpeed = null,
                    calorie = calories?.toDouble() ?: 0.0,
                    climb = null,
                    heartRate = heartRate?.toDouble() ?: 0.0,
                    comment = comments ?: "",
                    locationList = null
                )

                dao.insert(entry)
            }

            Toast.makeText(this, "Saved entry to database!", Toast.LENGTH_SHORT).show()
            finish()
        }

        cancelButton.setOnClickListener(){
            finish()
        }
    }

    private fun showDatePicker(onDateSelected: (year: Int, month: Int, day: Int) -> Unit) {
        val year = date.get(Calendar.YEAR)
        val month = date.get(Calendar.MONTH)
        val day = date.get(Calendar.DAY_OF_MONTH)

        val dpd = android.app.DatePickerDialog(this, { _, y, m, d ->
            onDateSelected(y, m, d)
        }, year, month, day)

        dpd.show()
    }

    private fun showTimePicker(onTimeSelected: (hourOfDay: Int, minute: Int) -> Unit) {
        val hour = time.get(Calendar.HOUR_OF_DAY)
        val minute = time.get(Calendar.MINUTE)

        val tpd = android.app.TimePickerDialog(this, { _, h, m ->
            onTimeSelected(h, m)
        }, hour, minute, false)

        tpd.show()
    }

    private fun showInputDialog(title: String, hint: String, dialogType: Int, onValueCaptured: (String) -> Unit) {
        val inputDialog = DialogFrag()
        val bundle = Bundle().apply {
            putInt(DialogFrag.DIALOG_KEY, dialogType)
            putString(DialogFrag.TITLE_KEY, title)
            putString(DialogFrag.LABEL_KEY, hint)
        }
        inputDialog.arguments = bundle
        inputDialog.setOnValueCapturedListener(object : DialogFrag.onValueCapturedListener {  // Fix: Lowercase 'o' to match DialogFrag
            override fun onValueCaptured(value: String) {
                onValueCaptured(value)
            }
        })
        inputDialog.show(supportFragmentManager, title)
    }
}
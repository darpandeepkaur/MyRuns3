package com.darpandeepkaur.darpandeepkaurmyruns3

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.MaterialToolbar
import com.darpandeepkaur.darpandeepkaurmyruns3.database.ExerciseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class DisplayEntryActivity : AppCompatActivity() {

    private lateinit var inputTypeText: TextView
    private lateinit var activityTypeText: TextView
    private lateinit var dateTimeText: TextView
    private lateinit var durationText: TextView
    private lateinit var distanceText: TextView
    private lateinit var calorieText: TextView
    private lateinit var heartRateText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_entry)

        // Toolbar delete button
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_delete) {
                val entryId = intent.getLongExtra("ENTRY_ID", -1)
                if (entryId != -1L) deleteEntry(entryId)
                true
            } else false
        }

        // Initialize TextViews
        inputTypeText = findViewById(R.id.inputTypeText)
        activityTypeText = findViewById(R.id.activityTypeText)
        dateTimeText = findViewById(R.id.dateTimeText)
        durationText = findViewById(R.id.durationText)
        distanceText = findViewById(R.id.distanceText)
        calorieText = findViewById(R.id.calorieText)
        heartRateText = findViewById(R.id.heartRateText)

        val entryId = intent.getLongExtra("ENTRY_ID", -1)
        if (entryId != -1L) loadEntry(entryId)
    }

    private fun loadEntry(entryId: Long) {
        lifecycleScope.launch(Dispatchers.IO) {
            val dao = ExerciseDatabase.getInstance(this@DisplayEntryActivity).exerciseDao()
            val entry = dao.getEntryById(entryId)
            entry?.let {
                val dateStr = SimpleDateFormat("HH:mm:ss MMM dd yyyy", Locale.getDefault())
                    .format(Date(it.dateTime))
                withContext(Dispatchers.Main) {
                    inputTypeText.text = "Manual Entry"
                    activityTypeText.text = getActivityName(it.activityType)
                    dateTimeText.text = dateStr
                    durationText.text = "${it.duration.toInt()} mins"
                    distanceText.text = "${it.distance} Miles"
                    calorieText.text = "${it.calorie?.toInt() ?: 0} cals"
                    heartRateText.text = "${it.heartRate?.toInt() ?: 0} bpm"
                }
            }
        }
    }

    private fun deleteEntry(entryId: Long) {
        lifecycleScope.launch(Dispatchers.IO) {
            val dao = ExerciseDatabase.getInstance(this@DisplayEntryActivity).exerciseDao()
            dao.deleteById(entryId)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@DisplayEntryActivity, "Entry deleted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun getActivityName(type: Int): String {
        return when (type) {
            0 -> "Running"
            1 -> "Walking"
            2 -> "Cycling"
            else -> "Downhill Skiing"
        }
    }
}
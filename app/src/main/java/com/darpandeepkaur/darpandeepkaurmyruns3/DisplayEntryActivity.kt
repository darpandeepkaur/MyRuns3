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
import androidx.activity.viewModels
import com.darpandeepkaur.darpandeepkaurmyruns3.database.ExerciseEntry
import java.util.*

class DisplayEntryActivity : AppCompatActivity() {

    private lateinit var inputTypeText: TextView
    private lateinit var activityTypeText: TextView
    private lateinit var dateTimeText: TextView
    private lateinit var durationText: TextView
    private lateinit var distanceText: TextView
    private lateinit var calorieText: TextView
    private lateinit var heartRateText: TextView
    private val viewModel: DisplayEntryViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_entry)

        setupToolbar()
        initViews()

        val entryId = intent.getLongExtra("ENTRY_ID", -1)
        if (viewModel.entry != null) {
            displayEntry(viewModel.entry!!)
        } else if (entryId != -1L) {
            loadEntry(entryId)
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_delete -> {
                    val entryId = intent.getLongExtra("ENTRY_ID", -1)
                    if (entryId != -1L) deleteEntry(entryId)
                    true
                }
                else -> false
            }
        }
    }

    private fun initViews() {
        inputTypeText = findViewById(R.id.inputTypeText)
        activityTypeText = findViewById(R.id.activityTypeText)
        dateTimeText = findViewById(R.id.dateTimeText)
        durationText = findViewById(R.id.durationText)
        distanceText = findViewById(R.id.distanceText)
        calorieText = findViewById(R.id.calorieText)
        heartRateText = findViewById(R.id.heartRateText)
    }

    private fun loadEntry(entryId: Long) {
        lifecycleScope.launch(Dispatchers.IO) {
            val dao = ExerciseDatabase.getInstance(this@DisplayEntryActivity).exerciseDao()
            val entry = dao.getEntryById(entryId)
            viewModel.entry = entry
            withContext(Dispatchers.Main) {
                entry?.let { displayEntry(it) }
            }
        }
    }

    private fun displayEntry(entry: ExerciseEntry) {
        val dateStr = SimpleDateFormat("HH:mm:ss MMM dd yyyy", Locale.getDefault())
            .format(Date(entry.dateTime))

        inputTypeText.text = "Manual Entry"
        activityTypeText.text = getActivityName(entry.activityType)
        dateTimeText.text = dateStr
        durationText.text = "${(entry.duration / 60).toInt()} mins ${(entry.duration % 60).toInt()} secs"
        distanceText.text = "${entry.distance} Miles"
        calorieText.text = "${entry.calorie?.toInt() ?: 0} cals"
        heartRateText.text = "${entry.heartRate?.toInt() ?: 0} bpm"
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
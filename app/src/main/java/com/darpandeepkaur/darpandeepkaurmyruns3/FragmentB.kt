package com.darpandeepkaur.darpandeepkaurmyruns3

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.darpandeepkaur.darpandeepkaurmyruns3.database.ExerciseDatabase
import com.darpandeepkaur.darpandeepkaurmyruns3.database.ExerciseEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class FragmentB : Fragment() {

    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val displayList = mutableListOf<String>()
    private var entries = listOf<ExerciseEntry>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_b, container, false)
        listView = view.findViewById(R.id.historyListView)
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_2, android.R.id.text1, displayList)
        listView.adapter = adapter

        loadHistory()

        listView.setOnItemClickListener { _, _, position, _ ->
            val entry = entries[position]
            val intent = Intent(requireContext(), DisplayEntryActivity::class.java)
            intent.putExtra("ENTRY_ID", entry.id)
            startActivity(intent)
        }

        return view
    }

    private fun loadHistory() {
        lifecycleScope.launch(Dispatchers.IO) {
            val dao = ExerciseDatabase.getInstance(requireContext()).exerciseDao()
            entries = dao.getAllEntries()

            val sdf = SimpleDateFormat("HH:mm:ss MMM dd yyyy", Locale.getDefault())

            val tempList = entries.map { entry ->
                val line1 = "${getInputType(entry.inputType)} Entry: ${getActivityType(entry.activityType)}, ${sdf.format(entry.dateTime)}"
                val line2 = "${entry.distance} Miles, ${entry.duration.toInt() / 60}mins ${entry.duration.toInt() % 60}secs"
                "$line1\n$line2"
            }

            withContext(Dispatchers.Main) {
                displayList.clear()
                displayList.addAll(tempList)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun getInputType(type: Int): String {
        return when (type) {
            0 -> "Manual"
            1 -> "GPS"
            2 -> "Automatic"
            else -> "Unknown"
        }
    }

    private fun getActivityType(type: Int): String {
        return when (type) {
            0 -> "Running"
            1 -> "Walking"
            2 -> "Cycling"
            3 -> "Hiking"
            4 -> "Downhill Skiing"
            5 -> "Cross-Country Skiing"
            6 -> "Snowboarding"
            7 -> "Skating"
            8 -> "Swimming"
            9 -> "Mountain Biking"
            10 -> "Wheelchair"
            11 -> "Elliptical"
            12 -> "Other"
            else -> "Unknown"
        }
    }

    override fun onResume() {
        super.onResume()
        loadHistory()
    }
}
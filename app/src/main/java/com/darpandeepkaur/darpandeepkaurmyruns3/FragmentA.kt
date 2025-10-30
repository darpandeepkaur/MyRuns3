package com.darpandeepkaur.darpandeepkaurmyruns3

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment


class FragmentA : Fragment() {

    private lateinit var inputSpinner: Spinner
    private lateinit var activitySpinner: Spinner
    private lateinit var startButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_a, container, false)

        inputSpinner = view.findViewById(R.id.InputTypeSpinner)
        activitySpinner = view.findViewById(R.id.ActivityTypeSpinner)
        startButton = view.findViewById(R.id.startButton)

        val inputs = listOf("Select Input", "Manual", "GPS", "Automatic")
        val activities = listOf(
            "Select Input",
            "Running",
            "Walking",
            "Standing",
            "Cycling",
            "Hiking",
            "Downhill Skiing",
            "Cross-Country Skiing",
            "Snowboarding",
            "Skating",
            "Swimming",
            "Mountain Biking",
            "Wheelchair",
            "Elliptical",
            "Other"
        )

        val inputAdapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, inputs)
        inputAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        inputSpinner.adapter = inputAdapter

        val activityAdapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            activities
        )
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        activitySpinner.adapter = activityAdapter

        startButton.setOnClickListener {
            val selected = inputSpinner.selectedItem?.toString() ?: ""
            when (selected) {
                "Manual" -> startActivity(Intent(requireContext(), StartActivity::class.java))
                "GPS", "Automatic" -> startActivity(
                    Intent(
                        requireContext(),
                        MapActivity::class.java
                    )
                )

                else -> Toast.makeText(
                    requireContext(),
                    "Please select an input",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return view

    }
}



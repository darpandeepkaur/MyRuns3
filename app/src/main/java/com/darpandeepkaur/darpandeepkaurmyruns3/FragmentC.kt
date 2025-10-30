package com.darpandeepkaur.darpandeepkaurmyruns3

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.net.Uri
import androidx.fragment.app.Fragment

class FragmentC : Fragment() {

    private lateinit var preferenceList: ListView
    private lateinit var settingsList: ListView
    private lateinit var miscList: ListView
    private lateinit var sharedPrefs: SharedPreferences

    private val accountItems = arrayOf("Name,Email,Class,etc", "Privacy Setting")
    private val additionalItems = arrayOf("Unit Preference", "Comments")
    private val miscItems = arrayOf("Webpage")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_c, container, false)

        val accountHeader: TextView = view.findViewById(R.id.AccountPreferences)
        val additionalHeader: TextView = view.findViewById(R.id.AdditionalSettings)
        val miscHeader: TextView = view.findViewById(R.id.Misc)
        preferenceList = view.findViewById(R.id.preferencesList)
        settingsList = view.findViewById(R.id.settingsList)
        miscList = view.findViewById(R.id.miscList)

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val accountAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, accountItems)
        preferenceList.adapter = accountAdapter

        val settingsAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, additionalItems )
        settingsList.adapter = settingsAdapter

        val miscAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, miscItems)
        miscList.adapter = miscAdapter

        preferenceList.setOnItemClickListener { _, _, position, id ->
            when (position) {
                0 -> {
                    startActivity(Intent(requireContext(), ProfileActivity::class.java))
                }

                1 -> {
                    val currentEnabled = sharedPrefs.getBoolean("privacy_enabled", true)
                    val newEnabled = !currentEnabled
                    sharedPrefs.edit().putBoolean("privacy_enabled", newEnabled).apply()
                }
            }

        }

        settingsList.setOnItemClickListener { _, _, position, id ->
            when (position) {
                0 -> showRadioDialog()
                1 -> showCommentsDialog()
            }
        }

        miscList.setOnItemClickListener { _, _, position, id ->
            when (position) {
                0 -> {
                    val url = "https://www.sfu.ca/"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                }
            }
        }

        return view
    }

    private fun showRadioDialog() {
        val radioGroup = RadioGroup(requireContext())
        val metric = RadioButton(requireContext()).apply { text = "Metric (Kilometers)"; id = 1 }
        val imperial = RadioButton(requireContext()).apply { text = "Imperial (Miles)"; id = 2 }
        radioGroup.addView(metric)
        radioGroup.addView(imperial)

        radioGroup.check(1)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Unit Preference")
            .setView(radioGroup)
            .setPositiveButton("OK") {_, _ ->
                val selectId = radioGroup.checkedRadioButtonId
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    private fun showCommentsDialog() {
        val editText = EditText(requireContext()).apply {
            maxLines = 4
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Comments")
            .setView(editText)
            .setPositiveButton("ok"){_, _->}

            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }
}
package com.darpandeepkaur.darpandeepkaurmyruns3

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat

class FragmentC : Fragment(R.layout.fragment_c) {

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Add the preference fragment dynamically
        childFragmentManager.beginTransaction()
            .replace(R.id.preferences_container, SettingsFragment())
            .commit()
    }
}

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
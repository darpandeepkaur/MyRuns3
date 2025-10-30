package com.darpandeepkaur.darpandeepkaurmyruns3

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class FragmentC : Fragment(R.layout.fragment_c) {

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load the inner settings fragment into the container
        childFragmentManager.beginTransaction()
            .replace(R.id.preferences_container, SettingsFragment())
            .commit()
    }
}

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var profileLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Register launcher to refresh summary when ProfileActivity finishes
        profileLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            // Reload SharedPreferences to update summary
            val prefs = requireContext().getSharedPreferences("profile_prefs", AppCompatActivity.MODE_PRIVATE)
            val name = prefs.getString("name", "")
            val email = prefs.getString("email", "")
            findPreference<Preference>("profile")?.summary = "$name\n$email"
        }
    }
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        // Profile
        val profilePref = findPreference<Preference>("profile")
        profilePref?.summary = "User Profile"
        profilePref?.setOnPreferenceClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
            true
        }

        // Webpage
        val webpagePref = findPreference<Preference>("webpage")
        webpagePref?.setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.sfu.ca/fas/computing.html")
            startActivity(intent)
            true
        }
    }
}
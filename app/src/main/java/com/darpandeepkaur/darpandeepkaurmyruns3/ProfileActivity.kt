package com.darpandeepkaur.darpandeepkaurmyruns3

// I referred to SharedPreferences in LayoutKotlin.zip and camera working in CameraDemoKotlin.zip

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.ActivityResult
import java.io.File
import androidx.lifecycle.ViewModel
import androidx.core.content.FileProvider
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.lifecycle.ViewModelProvider
import kotlin.toString


class ProfileActivity : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var phone: EditText
    private lateinit var classNumber: EditText
    private lateinit var radio_group: RadioGroup
    private lateinit var female_radio: RadioButton
    private lateinit var male_radio: RadioButton
    private lateinit var major: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var changeButton: Button
    private lateinit var profile_image: ImageView
    private lateinit var myViewModel: MyViewModel

    private lateinit var cameraResult: ActivityResultLauncher<Intent>
    private val PREF_NAME = "profile_prefs"
    private val NAME = "name"
    private val EMAIL = "email"
    private val PHONE = "phone"
    private val CLASS = "class"
    private val MAJOR = "major"
    private val GENDER_ID = "gender_id"
    private val PROFILE_URI = "profile uri"
    private val tempImageFileName = "profile.jpg"


    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            myViewModel.tempImgUri?.let { uri ->

                val bitmap = Util.getBitmap(this, uri)  // Uses your Util (with 90° rotation)
                myViewModel.userImage.value = bitmap
                saveImageUri(uri)  // Save URI for persistence

            }
        } else {
            Toast.makeText(this, "Camera capture failed", Toast.LENGTH_SHORT).show()
        }
    }
    private val pickFromGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val bmp = Util.getBitmap(this, it)
                myViewModel.userImage.value = bmp
                saveImageUri(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        saveButton = findViewById(R.id.save_button)
        cancelButton = findViewById(R.id.cancel_button)
        changeButton = findViewById(R.id.change_profile_button)
        name = findViewById(R.id.your_name)
        email = findViewById(R.id.email_input)
        phone = findViewById(R.id.phone_number)
        classNumber = findViewById(R.id.class_number)
        major = findViewById(R.id.your_major)
        radio_group = findViewById(R.id.radio_group)
        female_radio = findViewById(R.id.female_button)
        male_radio = findViewById(R.id.male_button)
        profile_image = findViewById(R.id.profile_photo)

        Util.checkPermissions(this)

        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        myViewModel.userImage.observe(this) { bmp ->
            bmp?.let { profile_image.setImageBitmap(it) }
        }

        val temporaryImageFile = File(getExternalFilesDir(null), tempImageFileName)
        myViewModel.tempImgUri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.fileprovider", temporaryImageFile)

        cameraResult = registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                myViewModel.tempImgUri?.let { uri ->
                    val bitmap = Util.getBitmap(this, uri)
                    myViewModel.userImage.value = bitmap
                }
            }
        }

        changeButton.setOnClickListener {
            val options = arrayOf("Take Photo", "Choose from Gallery")
            AlertDialog.Builder(this)
                .setTitle("Select Image Source")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> {
                            myViewModel.tempImgUri?.let { uri ->
                                cameraLauncher.launch(uri)
                            }
                        }
                        1 -> {
                            pickFromGalleryLauncher.launch("image/*")
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        saveButton.setOnClickListener(){
            val SharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
            val editor = SharedPreferences.edit()
            editor.putString(NAME, name.text.toString())
            editor.putString(EMAIL, email.text.toString())
            editor.putString(PHONE, phone.text.toString())
            editor.putString(CLASS, classNumber.text.toString())
            editor.putString(MAJOR, major.text.toString())
            editor.putInt(GENDER_ID, radio_group.checkedRadioButtonId)
            myViewModel.tempImgUri?.let { uri ->
                editor.putString(PROFILE_URI, uri.toString())
            }

            editor.apply()
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
            finish()
        }

        cancelButton.setOnClickListener(){
            finish()
        }

        val SharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        name.setText(SharedPreferences.getString(NAME, ""))
        email.setText(SharedPreferences.getString(EMAIL, ""))
        phone.setText(SharedPreferences.getString(PHONE, ""))
        classNumber.setText(SharedPreferences.getString(CLASS, ""))
        major.setText(SharedPreferences.getString(MAJOR, ""))
        SharedPreferences.getString(PROFILE_URI, null)?.let { uriStr ->
            val uri = Uri.parse(uriStr)
            myViewModel.tempImgUri = uri
            val bmp = Util.getBitmap(this, uri)
            profile_image.setImageBitmap(bmp)
        }

        val saveRadio = SharedPreferences.getInt(GENDER_ID, -1)
        if (saveRadio != -1) {
            radio_group.check(saveRadio)
        } else {
            radio_group.clearCheck()
        }

    }
    private fun saveImageUri(uri: Uri) {
        val sharedPrefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        sharedPrefs.edit().putString(PROFILE_URI, uri.toString()).apply()
    }
}
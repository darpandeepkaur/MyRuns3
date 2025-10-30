package com.darpandeepkaur.darpandeepkaurmyruns3

import androidx.lifecycle.ViewModel
import android.icu.util.Calendar

class StartViewModel : ViewModel() {
    var date: Calendar = Calendar.getInstance()
    var time: Calendar = Calendar.getInstance()
    var duration: Int? = null
    var distance: Int? = null
    var calories: Int? = null
    var heartRate: Int? = null
    var comments: String? = null
}
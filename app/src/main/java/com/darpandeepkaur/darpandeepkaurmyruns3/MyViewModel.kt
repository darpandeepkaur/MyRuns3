package com.darpandeepkaur.darpandeepkaurmyruns3

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    val userImage = MutableLiveData<Bitmap?>()
    var tempImgUri: Uri?=null
}
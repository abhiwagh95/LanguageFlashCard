package com.example.languageflashcard.ui.home

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Please Enter Word To Translate:"
    }
    val text: LiveData<String> = _text
}
package com.example.languageflashcard.ui.launch

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaunchViewModel @Inject constructor(private val firebaseAuth: FirebaseAuth) : ViewModel() {

    fun getCurrentUser() = firebaseAuth.currentUser
}
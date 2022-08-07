package com.example.languageflashcard.ui.home

import androidx.lifecycle.ViewModel
import com.example.languageflashcard.model.Translate
import com.example.languageflashcard.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val firebaseAuth: FirebaseAuth
) :
    ViewModel() {

    fun addTranslateToFirebase(translate: Translate) =
        firebaseRepository.addTranslateToFirebase(translate)

    fun currentUserUUID() = firebaseAuth.currentUser!!.uid
}
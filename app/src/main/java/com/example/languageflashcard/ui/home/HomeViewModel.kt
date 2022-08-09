package com.example.languageflashcard.ui.home

import androidx.lifecycle.ViewModel
import com.example.languageflashcard.model.Translate
import com.example.languageflashcard.repository.FirebaseRepository
import com.example.languageflashcard.repository.GoogleAPIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val googleAPIRepository: GoogleAPIRepository,
) :
    ViewModel() {

    var currentTranslate: Translate = Translate()

    fun addTranslateToFirebase(translate: Translate) =
        firebaseRepository.addTranslateToFirebase(translate)

    fun currentUserUUID() = firebaseRepository.getCurrentUserUUID()

    suspend fun getGoogleAPIResponse(
        query: String,
        target: String = "ja",
        source: String = "en"
    ) = googleAPIRepository.getGoogleAPIResponse(query = query, target = target, source = source)
}

package com.example.languageflashcard.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Translate(
    val originalWord: String = "",
    val translatedWord: String = "",
    @ServerTimestamp
    val date: Timestamp = Timestamp(Date()),
    val userId: String = "",
    val originalLanguage: String = "",
    val translatedLanguage: String = "",
)

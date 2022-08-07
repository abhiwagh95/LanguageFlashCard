package com.example.languageflashcard.model

import com.google.firebase.Timestamp
import java.util.*

data class Translate(
    val originalWord: String = "",
    val translatedWord: String = "",
    val date: Timestamp = Timestamp(Date()),
    val userId: String = "",
    val originalLanguage: String = "",
    val translatedLanguage: String = "",
)

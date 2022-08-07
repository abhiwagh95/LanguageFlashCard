package com.example.languageflashcard.model

data class GoogleAPIResponse(
    val data: Data
)

data class Data(
    val translations: List<Translation>
)

data class Translation(
    val translatedText: String
)

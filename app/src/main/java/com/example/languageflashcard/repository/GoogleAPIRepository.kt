package com.example.languageflashcard.repository

import com.example.languageflashcard.BuildConfig
import com.example.languageflashcard.repository.retrofit.GoogleAPIService
import javax.inject.Inject

class GoogleAPIRepository @Inject constructor(
    private val googleAPIService: GoogleAPIService
) {

    suspend fun getGoogleAPIResponse(
        query: String,
        target: String,
        source: String
    ) = googleAPIService.getGoogleAPIResponse(
        key = BuildConfig.GOOGLE_API_KEY,
        query = query,
        target = target,
        format = "text",
        source = source
    )

}
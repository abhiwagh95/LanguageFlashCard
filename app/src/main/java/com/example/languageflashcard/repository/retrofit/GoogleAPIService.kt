package com.example.languageflashcard.repository.retrofit

import com.example.languageflashcard.model.GoogleAPIResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface GoogleAPIService {
    @GET("language/translate/v2")
    suspend fun getGoogleAPIResponse(
        @Query("key") key: String,
        @Query("q") query: String,
        @Query("target") target: String,
        @Query("format") format: String,
        @Query("source") source: String
    ): GoogleAPIResponse
}
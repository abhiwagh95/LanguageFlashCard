package com.example.languageflashcard.repository

import com.example.languageflashcard.BuildConfig
import com.example.languageflashcard.model.Response
import com.example.languageflashcard.repository.retrofit.GoogleAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GoogleAPIRepository @Inject constructor(
    private val googleAPIService: GoogleAPIService
) {

    suspend fun getGoogleAPIResponse(
        query: String,
        target: String,
        source: String
    ) = flow {
        emit(Response.Loading())

        val googleAPIResponse = withContext(Dispatchers.Default) {
            googleAPIService.getGoogleAPIResponse(
                query = query, target = target, source = source,
                format = "text", key = BuildConfig.GOOGLE_API_KEY
            )
        }
        emit(Response.Success(googleAPIResponse))
    }.catch {
        emit(Response.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}
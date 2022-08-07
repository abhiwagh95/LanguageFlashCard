package com.example.languageflashcard.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languageflashcard.model.GoogleAPIResponse
import com.example.languageflashcard.model.Response
import com.example.languageflashcard.model.Translate
import com.example.languageflashcard.repository.FirebaseRepository
import com.example.languageflashcard.repository.GoogleAPIRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val googleAPIRepository: GoogleAPIRepository,
    private val firebaseAuth: FirebaseAuth
) :
    ViewModel() {

    fun addTranslateToFirebase(translate: Translate) =
        firebaseRepository.addTranslateToFirebase(translate)

    fun currentUserUUID() = firebaseAuth.currentUser!!.uid

    suspend fun getGoogleAPIResponse(
        query: String,
        target: String = "ja",
        source: String = "en"
    ) = flow<Response<GoogleAPIResponse>> {
        emit(Response.Loading())

        val googleAPIResponseDeferred = viewModelScope.async {
            googleAPIRepository.getGoogleAPIResponse(
                query = query, target = target, source = source
            )
        }
        emit(Response.Success(googleAPIResponseDeferred.await()))
    }.catch {
        emit(Response.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}
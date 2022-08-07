package com.example.languageflashcard.repository

import com.example.languageflashcard.constants.FirebaseConstants
import com.example.languageflashcard.model.Response
import com.example.languageflashcard.model.Translate
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepository @Inject constructor(private val firebaseFirestore: FirebaseFirestore) {

    fun addTranslateToFirebase(translate: Translate) = flow<Response<DocumentReference>> {
        emit(Response.Loading())
        val rootCollectionReference =
            firebaseFirestore.collection(FirebaseConstants.ROOT_COLLECTION)
        val documentReference = rootCollectionReference.add(translate).await()
        emit(Response.Success(documentReference))
    }.catch {
        emit(Response.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}
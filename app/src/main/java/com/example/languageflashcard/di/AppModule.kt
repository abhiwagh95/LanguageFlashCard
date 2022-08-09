package com.example.languageflashcard.di

import com.example.languageflashcard.repository.FirebaseRepository
import com.example.languageflashcard.repository.GoogleAPIRepository
import com.example.languageflashcard.repository.retrofit.GoogleAPIService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        val firebaseFirestore = Firebase.firestore
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()

        firebaseFirestore.firestoreSettings = settings
        return firebaseFirestore
    }

    @Provides
    @Singleton
    fun provideFirebaseRepository(
        firebaseFirestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): FirebaseRepository {
        return FirebaseRepository(
            firebaseFirestore = firebaseFirestore,
            firebaseAuth = firebaseAuth
        )
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://translation.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGoogleAPIService(retrofit: Retrofit): GoogleAPIService {
        return retrofit.create(GoogleAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideGoogleAPIRepository(googleAPIService: GoogleAPIService): GoogleAPIRepository {
        return GoogleAPIRepository(googleAPIService)
    }
}
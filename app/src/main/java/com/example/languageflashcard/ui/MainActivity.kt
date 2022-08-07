package com.example.languageflashcard.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.languageflashcard.R
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mainViewModel.getCurrentUser()
        updateActivityBasedOnLoginStatus(currentUser)
    }

    private fun updateActivityBasedOnLoginStatus(currentUser: FirebaseUser?) {
        currentUser?.let {
            startActivity(Intent(this@MainActivity, NavigationDrawerActivity::class.java))
            finish()
        } ?: kotlin.run {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
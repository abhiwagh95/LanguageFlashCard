package com.example.languageflashcard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
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
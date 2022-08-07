package com.example.languageflashcard.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.languageflashcard.R
import com.example.languageflashcard.databinding.ActivityLoginBinding
import com.example.languageflashcard.ui.NavigationDrawerActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        setUpGoogleLoginUI()
        setUpGoogleLoginBuilders()
    }

    private fun setUpGoogleLoginUI() {
        binding.signInButton.setSize(SignInButton.SIZE_STANDARD)
        binding.signInButton.setOnClickListener { startGoogleSignInProcess() }
    }

    private fun setUpGoogleLoginBuilders() {
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    private fun startGoogleSignInProcess() {
        lifecycleScope.launch {
            try {
                val beginSignInResult = oneTapClient.beginSignIn(signInRequest).await()
                val intentSenderRequest =
                    IntentSenderRequest.Builder(beginSignInResult.pendingIntent.intentSender)
                        .build()
                googleSignInActivityResult.launch(intentSenderRequest)
            } catch (exception: Exception) {
                Toast.makeText(
                    baseContext,
                    "Error occurred during Google SignIn ${exception.message}",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }

    private val googleSignInActivityResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val googleCredential = oneTapClient.getSignInCredentialFromIntent(result.data)
                    googleCredential.googleIdToken?.let {
                        loginWithFirebase(it)
                    }
                } catch (exception: Exception) {
                    Toast.makeText(
                        this,
                        "Error occurred during Google SignIn ${exception.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    private fun loginWithFirebase(idToken: String) {
        lifecycleScope.launch {
            changeProgressBarVisibility(View.VISIBLE)
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            try {
                firebaseAuth.signInWithCredential(credential).await()
                changeProgressBarVisibility(View.GONE)
                startActivity(
                    Intent(
                        this@LoginActivity,
                        NavigationDrawerActivity::class.java
                    )
                )
                finish()
            } catch (exception: Exception) {
                Toast.makeText(
                    baseContext,
                    "Firebase Authentication Failed ${exception.message}",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }

    private fun changeProgressBarVisibility(visibility: Int) {
        binding.progressBar.visibility = visibility
    }
}
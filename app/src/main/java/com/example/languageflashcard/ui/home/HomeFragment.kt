package com.example.languageflashcard.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.languageflashcard.databinding.FragmentHomeBinding
import com.example.languageflashcard.model.Response
import com.example.languageflashcard.model.Translate
import com.google.firebase.Timestamp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.buttonSearch.setOnClickListener {
            if (binding.inputText.text.toString().isEmpty()) {
                Toast.makeText(context, "Please enter word to search", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            getGoogleAPIResponse(binding.inputText.text.toString())
        }
        binding.buttonAddToFirestore.setOnClickListener {
            addTranslateToFirebase()
        }
    }

    private fun getGoogleAPIResponse(query: String) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.getGoogleAPIResponse(query = query).collect { response ->
                    when (response) {
                        is Response.Loading -> binding.progressBar.visibility = View.VISIBLE
                        is Response.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.translatedTextview.visibility = View.GONE
                            Toast.makeText(context, "Error Occurred", Toast.LENGTH_SHORT).show()
                        }
                        is Response.Success -> {
                            val translatedWord = response.data.data.translations[0].translatedText
                            binding.progressBar.visibility = View.GONE
                            binding.translatedTextview.visibility = View.VISIBLE
                            binding.buttonSearch.visibility = View.GONE
                            binding.buttonAddToFirestore.visibility = View.VISIBLE
                            binding.translatedTextview.text = translatedWord

                            homeViewModel.currentTranslate = Translate(
                                originalWord = query,
                                translatedWord = translatedWord,
                                date = Timestamp.now(),
                                userId = homeViewModel.currentUserUUID()!!,
                                originalLanguage = "en",
                                translatedLanguage = "ja"
                            )
                        }
                    }
                }

            }
        }
    }

    private fun addTranslateToFirebase() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.addTranslateToFirebase(translate = homeViewModel.currentTranslate)
                    .collect { response ->
                        when (response) {
                            is Response.Loading -> binding.progressBar.visibility = View.VISIBLE
                            is Response.Error -> {
                                Toast.makeText(
                                    context,
                                    "Error Occurred",
                                    Toast.LENGTH_LONG
                                ).show()
                                binding.progressBar.visibility = View.GONE
                            }
                            is Response.Success -> {
                                Toast.makeText(
                                    context,
                                    "Success: ${response.data.id}",
                                    Toast.LENGTH_LONG
                                ).show()
                                binding.progressBar.visibility = View.GONE
                            }
                        }
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
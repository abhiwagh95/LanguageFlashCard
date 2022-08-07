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
        initUI()
        return binding.root
    }

    private fun initUI() {
        binding.buttonSearch.text = "SEARCH"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.buttonSearch.setOnClickListener {
            val currentWord = binding.inputText.text.toString()
            val translate = Translate(
                originalWord = currentWord,
                translatedWord = "translatedWord",
                date = Timestamp.now(),
                userId = homeViewModel.currentUserUUID(),
                originalLanguage = "EN",
                translatedLanguage = "JP"
            )
            addTranslateToFirebase(translate = translate)
        }
    }

    private fun addTranslateToFirebase(translate: Translate) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.addTranslateToFirebase(translate = translate)
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
package com.example.languageflashcard.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.languageflashcard.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.textviewMessage
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        search()
        return root
    }

    private fun search() {
        val inputSearch: EditText = binding.inputText
        val buttonSearch: Button = binding.buttonSearch
        buttonSearch.text = "SEARCH"
        buttonSearch.setOnClickListener {
            Toast.makeText(
                context,
                "Result will be displayed shortly : " + inputSearch.text,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
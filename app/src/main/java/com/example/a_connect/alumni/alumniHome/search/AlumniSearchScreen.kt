package com.example.a_connect.alumni.alumniHome.search

import android.content.Context
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import com.example.a_connect.databinding.FragmentAlumniSearchScreenBinding

class AlumniSearchScreen : Fragment() {
    private lateinit var binding: FragmentAlumniSearchScreenBinding
    private lateinit var searchView: SearchView
    private lateinit var searchViewModel: AlumniSearchViewModel
    private lateinit var alumniAdapter: AlumniSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlumniSearchScreenBinding.inflate(inflater, container, false)

        // Initialize ViewModel
        searchViewModel = ViewModelProvider(this)[AlumniSearchViewModel::class.java]
        searchView = binding.searchView

        // RecyclerView setup
        alumniAdapter = AlumniSearchAdapter { selectedAlumni ->
            openAlumniProfile(selectedAlumni) // Handle alumni profile click
        }
        binding.resultsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.resultsRecyclerView.adapter = alumniAdapter

        // Observe ViewModel data
        searchViewModel.alumniList.observe(viewLifecycleOwner, Observer { alumni ->
            alumniAdapter.submitList(alumni) // Efficiently update the list
        })

        // Automatically open SearchView and show keyboard when fragment starts
        binding.root.post {
            // Focus SearchView and show the keyboard immediately after the fragment is created
            searchView.requestFocus()
            showKeyboard(searchView)
        }

        // Listen to user input and trigger search immediately
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchViewModel.searchAlumni(it) // Trigger search when submitting query
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.trim().isEmpty()) {
                        // If the query is empty, clear the list (show no results)
                        searchViewModel.clearAlumniList() // Function to clear the list
                    } else {
                        // Trigger search only when query is non-empty
                        searchViewModel.searchAlumni(it.trim()) // Trigger search on text change
                    }
                }
                return true
            }
        })

        return binding.root
    }

    private fun openAlumniProfile(alumni: AlumniSearchDataClass) {
        // Implement navigation to Alumni Profile Fragment
    }

    // Function to show keyboard when the SearchView is focused
    private fun showKeyboard(view: View) {
        view.post {
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}

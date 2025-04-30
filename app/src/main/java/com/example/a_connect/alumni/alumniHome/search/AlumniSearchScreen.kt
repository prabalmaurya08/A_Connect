package com.example.a_connect.alumni.alumniHome.search

import android.content.Context
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.example.a_connect.R
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

        // Expand search view and show keyboard initially
        binding.root.post {
            searchView.isIconified = false
            searchView.requestFocus()
            showKeyboard(searchView)
        }

        // Observe alumni list
        searchViewModel.alumniList.observe(viewLifecycleOwner, Observer { alumni ->
            if (alumni.isEmpty()) {
                showEmptyState()
            } else {
                showResults()
                alumniAdapter.submitList(alumni)
            }
        })

        // SearchView listeners
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    startLoading()
                    searchViewModel.searchAlumni(it)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.trim().isEmpty()) {
                        searchViewModel.clearAlumniList()
                        hideResults()
                    } else {
                        startLoading()
                        searchViewModel.searchAlumni(it.trim())
                    }
                }
                return true
            }
        })

        return binding.root
    }

    private fun openAlumniProfile(alumni: AlumniSearchDataClass) {
        // Log email before passing to the next screen
        Log.d("AlumniSearchScreen", "Sending email: ${alumni.email}")

        val bundle = Bundle().apply {
            putString("email", alumni.email)
        }
        findNavController().navigate(R.id.alumniProfileDetail, bundle)
    }


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

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard()
    }

    /** --- Additional functions to handle shimmer --- **/

    private fun startLoading() {
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.shimmerLayout.startShimmer()
        binding.resultsRecyclerView.visibility = View.GONE
    }

    private fun showResults() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.resultsRecyclerView.visibility = View.VISIBLE
    }

    private fun hideResults() {
        binding.resultsRecyclerView.visibility = View.GONE
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
    }

    private fun showEmptyState() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.resultsRecyclerView.visibility = View.GONE
        // You can show a "no alumni found" message here if you want
    }

}


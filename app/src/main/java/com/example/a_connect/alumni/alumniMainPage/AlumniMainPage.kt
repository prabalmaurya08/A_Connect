package com.example.a_connect.alumni.alumniMainPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.a_connect.R
import com.example.a_connect.alumni.alumniCommunity.AluminiCommunityPage
import com.example.a_connect.databinding.FragmentAlumniMainPageBinding

import com.example.a_connect.alumni.alumniHome.AlumniHomePage
import com.example.a_connect.alumni.alumniJob.AlumniJob

import com.example.a_connect.alumni.alumniPost.AlumniPost
import com.example.a_connect.alumni.alumniProfile.AlumniProfile

import com.google.android.material.bottomnavigation.BottomNavigationView

class AlumniMainPage : Fragment() {
    private lateinit var binding: FragmentAlumniMainPageBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlumniMainPageBinding.inflate(inflater, container, false)

        bottomNavigationView = binding.aluminiMainPageBottomNav

        // Set up bottom navigation listener
        setupBottomNavigation()

        // Load the default fragment (Home)
        loadFragment(AlumniHomePage())

        return binding.root
    }

    private fun setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_home -> loadFragment(AlumniHomePage())
                R.id.bottom_nav_community -> loadFragment(AluminiCommunityPage())
                R.id.bottom_nav_post -> loadFragment(AlumniPost())
                R.id.bottom_nav_job -> loadFragment(AlumniJob())
                R.id.bottom_nav_profile -> loadFragment(AlumniProfile())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        // Replace the existing fragment in FragmentContainerView
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }
}

package com.example.a_connect.student.studentMainPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction

import com.example.a_connect.R

import com.example.a_connect.databinding.FragmentStudentMainPageBinding
import com.example.a_connect.student.studentCommunity.StudentCommunity
import com.example.a_connect.student.studentExplore.StudentExplore
import com.example.a_connect.student.studentHomePage.StudentHomePage
import com.example.a_connect.student.studentJob.StudentJob
import com.example.a_connect.student.studentProfile.StudentProfile
import com.google.android.material.bottomnavigation.BottomNavigationView


class StudentMainPage : Fragment() {
    private lateinit var binding: FragmentStudentMainPageBinding

    private lateinit var bottomNavigationView: BottomNavigationView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentStudentMainPageBinding.inflate(layoutInflater)


        bottomNavigationView = binding.bottomNav




        setupViewPagerWithBottomNavigation()
        return binding.root
    }
    private fun setupViewPagerWithBottomNavigation() {

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_home-> loadFragment(StudentHomePage())
                R.id.bottom_nav_community -> loadFragment(StudentCommunity())
                R.id.bottom_nav_explore ->loadFragment(StudentExplore())
                R.id.bottom_nav_job -> loadFragment(StudentJob())
                R.id.bottom_nav_profile ->loadFragment(StudentProfile())
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
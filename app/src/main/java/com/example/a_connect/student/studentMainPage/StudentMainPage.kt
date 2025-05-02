package com.example.a_connect.student.studentMainPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2

import com.example.a_connect.R
import com.example.a_connect.alumni.alumniExplore.AlumniMap
import com.example.a_connect.alumni.alumniMainPage.AlumniMainPageViewPagerAdapter

import com.example.a_connect.databinding.FragmentStudentMainPageBinding
import com.example.a_connect.student.studentCommunity.StudentCommunity
import com.example.a_connect.student.studentExplore.StudentExplore
import com.example.a_connect.student.studentHomePage.StudentHomePage
import com.example.a_connect.student.studentJob.StudentJob
import com.example.a_connect.student.studentProfile.StudentProfile
import com.google.android.material.bottomnavigation.BottomNavigationView


class StudentMainPage : Fragment() {
   private var _binding: FragmentStudentMainPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var adapter: StudentMainPageAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStudentMainPageBinding.inflate(inflater, container, false)


        bottomNavigationView = binding.bottomNav
        viewPager = binding.studentMainPageViewPager
        setupViewPager()




     setupBottomNavigation()
        return binding.root
    }

    private fun setupViewPager() {
        adapter = StudentMainPageAdapter(this)
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })
    }

    private fun setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.s_bottom_nav_home -> viewPager.currentItem = 0
                R.id.s_bottom_nav_community -> viewPager.currentItem = 1
                R.id.s_bottom_nav_explore-> viewPager.currentItem = 2
                R.id.s_bottom_nav_job -> viewPager.currentItem = 3
                R.id.s_bottom_nav_profile -> viewPager.currentItem = 4
            }
            true
        }
    }





}
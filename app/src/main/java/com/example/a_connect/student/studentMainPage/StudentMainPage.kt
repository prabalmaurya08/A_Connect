package com.example.a_connect.student.studentMainPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.a_connect.R
import com.example.a_connect.alumni.alumniMainPage.AlumniMainPageViewPagerAdapter
import com.example.a_connect.databinding.FragmentStudentMainPageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class StudentMainPage : Fragment() {
    private lateinit var binding: FragmentStudentMainPageBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var studentMainPageViewPagerAdapter:StudentMainPageViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentStudentMainPageBinding.inflate(layoutInflater)

        viewPager = binding.viewPager
        bottomNavigationView = binding.bottomNav

        // Set up ViewPager with FragmentStateAdapter
       studentMainPageViewPagerAdapter = StudentMainPageViewPagerAdapter(this)
        viewPager.adapter = studentMainPageViewPagerAdapter

        setupViewPagerWithBottomNavigation()
        return binding.root
    }
    private fun setupViewPagerWithBottomNavigation() {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_home-> viewPager.currentItem = 0
                R.id.bottom_nav_community -> viewPager.currentItem = 1
                R.id.bottom_nav_explore -> viewPager.currentItem = 2
                R.id.bottom_nav_job -> viewPager.currentItem = 3
                R.id.bottom_nav_profile -> viewPager.currentItem = 4
            }
            true
        }
    }




}
package com.example.a_connect.admin.adminMainPage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.a_connect.R
import com.example.a_connect.admin.adminCollegeProfile.AdminCollegeProfile
import com.example.a_connect.alumni.alumniMainPage.AlumniMainPageViewPagerAdapter
import com.example.a_connect.databinding.FragmentAdminMainpageBinding
import com.example.a_connect.databinding.FragmentAlumniMainPageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class AdminMainPage : Fragment(),AdminCollegeProfile.OnAdminEditProfileClickListener {
    private lateinit var binding: FragmentAdminMainpageBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var alumniMainPageViewPagerAdapter: AdminMainPageViewPagerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAdminMainpageBinding.inflate(layoutInflater)
        viewPager = binding.viewPager
        bottomNavigationView = binding.bottomNav

        // Set up ViewPager with FragmentStateAdapter
        alumniMainPageViewPagerAdapter = AdminMainPageViewPagerAdapter(this)
        viewPager.adapter = alumniMainPageViewPagerAdapter

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
                R.id.bottom_nav_announcement -> viewPager.currentItem = 1
                R.id.bottom_nav_events -> viewPager.currentItem = 2
                R.id.bottom_nav_job-> viewPager.currentItem = 3
                R.id.bottom_nav_college-> viewPager.currentItem = 4
            }
            true
        }
    }

    override fun onAdminEditProfileClicked() {
        try {


            findNavController().navigate(R.id.action_adminMainPage_to_adminEditProfile)
        } catch (e: Exception) {
            Log.e("AdminMainScreen", "Navigation failed: ${e.message}")
        }
    }


}
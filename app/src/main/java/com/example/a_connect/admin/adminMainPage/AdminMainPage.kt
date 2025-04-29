package com.example.a_connect.admin.adminMainPage


import android.content.Intent
import android.os.Bundle
import android.view.*

import androidx.activity.OnBackPressedCallback

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import androidx.viewpager2.widget.ViewPager2
import com.example.a_connect.R


import com.example.a_connect.databinding.FragmentAdminMainpageBinding
import com.example.a_connect.login.AlumniLogin

import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.core.view.get


class AdminMainPage : Fragment() {

    private var _binding: FragmentAdminMainpageBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var adapter: AdminMainPageViewPagerAdapter





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAdminMainpageBinding.inflate(inflater, container, false)
        viewPager = binding.adminViewPager
        bottomNavigationView = binding.bottomNav

        setupViewPager()
        setupBottomNavigation()







        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController()

                if (!navController.navigateUp()) {
                    requireActivity().finish() // Exit app if no back stack
                }
            }
        })
    }


    private fun setupViewPager() {
        adapter = AdminMainPageViewPagerAdapter(this)
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNavigationView.menu[position].isChecked = true
            }
        })
    }

    private fun setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.a_bottom_nav_home -> viewPager.currentItem = 0
                R.id.a_bottom_nav_announcement -> viewPager.currentItem = 1
                R.id.a_bottom_nav_events -> viewPager.currentItem = 2
                R.id.a_bottom_nav_job -> viewPager.currentItem = 3
                R.id.a_bottom_nav_college -> viewPager.currentItem = 4
            }
            true
        }
    }






    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

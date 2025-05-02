package com.example.a_connect.alumni.alumniMainPage


import android.content.Intent
import android.os.Bundle
import android.view.*

import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.a_connect.R

import com.example.a_connect.databinding.FragmentAlumniMainPageBinding
import com.example.a_connect.login.AlumniLogin

import com.google.android.material.bottomnavigation.BottomNavigationView

class AluminiMainPage : Fragment() {

    private var _binding: FragmentAlumniMainPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var adapter: AlumniMainPageViewPagerAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlumniMainPageBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize Views
        viewPager = binding.alumniMainPageViewPager
        bottomNavigationView = binding.alumniMainPageBottomNav






        setupViewPager()
        setupBottomNavigation()


        return view
    }


    private fun setupViewPager() {
        adapter = AlumniMainPageViewPagerAdapter(this)
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
                R.id.bottom_nav_home -> viewPager.currentItem = 0
                R.id.bottom_nav_community -> viewPager.currentItem = 1
                R.id.bottom_nav_post -> viewPager.currentItem = 2
                R.id.bottom_nav_job -> viewPager.currentItem = 3
                R.id.bottom_nav_profile -> viewPager.currentItem = 4
            }
            true
        }
    }




    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.alumniMainPageViewPager, fragment)
            .addToBackStack(null)
            .commit()

    }


    private fun logoutUser() {
        val intent = Intent(requireContext(), AlumniLogin::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finishAffinity()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

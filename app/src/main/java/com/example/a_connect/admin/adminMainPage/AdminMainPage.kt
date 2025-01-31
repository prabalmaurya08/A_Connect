@file:Suppress("SameParameterValue", "UNUSED_EXPRESSION")

package com.example.a_connect.admin.adminMainPage

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.a_connect.R
import com.example.a_connect.databinding.FragmentAdminMainpageBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminMainPage : Fragment() {

    private var _binding: FragmentAdminMainpageBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var adapter: AdminMainPageViewPagerAdapter

    private lateinit var defaultTopBar: MaterialToolbar
    private lateinit var searchTopBar: View
    private lateinit var searchInput: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminMainpageBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize Views
        viewPager = binding.viewPager
        bottomNavigationView = binding.bottomNav
        defaultTopBar = binding.defaultTopBar
        searchTopBar = binding.searchTopBar
        searchInput = binding.searchInput

        // Set up ViewPager2 Adapter
        adapter = AdminMainPageViewPagerAdapter(this)
        viewPager.adapter = adapter

        // Disable swipe navigation (optional)
        viewPager.isUserInputEnabled = false

        // Bottom Navigation Listener
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_home -> switchToDefaultBar("Dashboard", 0)
                R.id.bottom_nav_announcement -> switchToDefaultBar("Announcements", 1)
                R.id.bottom_nav_events -> switchToDefaultBar("Events", 2)
                R.id.bottom_nav_job -> switchToSearchBar(3) // Show search bar for Job page
                R.id.bottom_nav_college -> switchToDefaultBar("College Profile", 4)
                else -> false
            }
            true
        }

        // Sync Bottom Navigation with ViewPager2
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })

        return view
    }

    // Show Default Top Bar with Title
    private fun switchToDefaultBar(title: String, position: Int) {
        viewPager.currentItem = position
        defaultTopBar.visibility = View.VISIBLE
        searchTopBar.visibility = View.GONE
        defaultTopBar.title = title
    }

    // Show Search Bar for AdminJob Page
    private fun switchToSearchBar(position: Int) {
        viewPager.currentItem = position
        defaultTopBar.visibility = View.GONE
        searchTopBar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
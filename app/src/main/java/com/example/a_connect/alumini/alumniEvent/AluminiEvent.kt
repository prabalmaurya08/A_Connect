package com.example.a_connect.alumini.alumniEvent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a_connect.databinding.FragmentAluminiEventBinding
import com.google.android.material.tabs.TabLayoutMediator

class AluminiEvent : Fragment() {

    private var _binding: FragmentAluminiEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAluminiEventBinding.inflate(inflater, container, false)

        // Initialize view pager Adapter
        val viewpageradapter = AlumniEventsPagerAdapter(this)
        binding.viewPager.adapter = viewpageradapter

        // Tab titles
        val tabTitles = listOf("Upcoming Events", "Past Events")

        // Attach TabLayout with ViewPager2
        TabLayoutMediator(binding.eventTabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        return binding.root // Return the binding root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}

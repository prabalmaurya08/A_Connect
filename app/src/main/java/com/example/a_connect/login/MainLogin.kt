package com.example.a_connect.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.a_connect.R
import com.example.a_connect.databinding.FragmentMainLoginBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainLogin : Fragment() {
    private lateinit var binding: FragmentMainLoginBinding




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentMainLoginBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment

        viewPagerWithTabLayout()

        return binding.root
    }


    private fun viewPagerWithTabLayout() {
        binding.mainLoginViewPager.adapter = MainLoginViewPagerAdapter(this)
        val tabLayout = binding.mainLoginTabLayout
        val viewPager = binding.mainLoginViewPager

        // Setup TabLayout with ViewPager2
        TabLayoutMediator(
            tabLayout, viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> tab.setText("Alumni")
                1 -> tab.setText("Student")
            }
        }.attach()

        // Set Tab Selected Listener
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(), R.color.blue))
                    1 -> tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(), R.color.green))
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }


}
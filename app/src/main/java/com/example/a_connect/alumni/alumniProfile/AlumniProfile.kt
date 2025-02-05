package com.example.a_connect.alumni.alumniProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.a_connect.R
import com.example.a_connect.SharedPreferencesHelper
import com.example.a_connect.admin.adminJob.JobTabAdapter
import com.example.a_connect.alumni.alumniPost.mvvm.AlumniPostViewmodel
import com.example.a_connect.databinding.FragmentAlumniProfileBinding
import com.google.android.material.tabs.TabLayoutMediator


class AlumniProfile : Fragment() {

    private lateinit var binding: FragmentAlumniProfileBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var currentUserEmail: String
    private lateinit var profileViewModel: AlumniPostViewmodel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAlumniProfileBinding.inflate(inflater,container,false)
        profileViewModel = ViewModelProvider(this)[AlumniPostViewmodel::class.java]
        currentUserEmail = SharedPreferencesHelper.getCurrentUserEmail() ?: return binding.root
        swipeRefreshLayout = binding.swipeRefreshLayout

        swipeRefreshLayout.setOnRefreshListener {

            profileViewModel.loadUserPosts(currentUserEmail) // Refresh posts
            swipeRefreshLayout.isRefreshing = false // Stop refresh animation
        }
        setUpViewPagerAdapter()
        return binding.root
    }

    private fun setUpViewPagerAdapter() {
        val adapter = AlumniProfileViewPagerAdapter(requireActivity())
        binding.alumniProfileViewPager.adapter = adapter

        // Attach TabLayout to ViewPager
        TabLayoutMediator(binding.alumniProfileTabLayout, binding.alumniProfileViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "MileStone"
                1 -> "Posts"
                else -> null
            }
        }.attach()
    }


}
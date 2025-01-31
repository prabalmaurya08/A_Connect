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
import com.example.a_connect.admin.adminEvent.AdminEvent
import com.example.a_connect.admin.adminHome.AdminHome
import com.example.a_connect.admin.adminJob.AdminAddJob
import com.example.a_connect.admin.adminJob.AdminJob
import com.example.a_connect.admin.adminJob.AdminJobDirections
import com.example.a_connect.admin.adminJob.OngoingJob
import com.example.a_connect.admin.adminNews.AdminNewsAnnouncement
import com.example.a_connect.alumni.alumniMainPage.AlumniMainPageViewPagerAdapter
import com.example.a_connect.databinding.FragmentAdminMainpageBinding
import com.example.a_connect.databinding.FragmentAlumniMainPageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class AdminMainPage : Fragment() ,AdminCollegeProfile.OnGoToEditProfileClickListener,AdminJob.OnGoToCreateJobClickListener{
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


//        // Set up ViewPager with FragmentStateAdapter
//        alumniMainPageViewPagerAdapter = AdminMainPageViewPagerAdapter(this)
//        viewPager.adapter = alumniMainPageViewPagerAdapter



        // Load default fragment on creation
        if (savedInstanceState == null) {
            loadFragment(AdminHome())
        }

        // Bottom Navigation Listener
        val bottomNav: BottomNavigationView = binding.root.findViewById(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_home -> loadFragment(AdminHome())
                R.id.bottom_nav_announcement -> loadFragment(AdminNewsAnnouncement())
                R.id.bottom_nav_events-> loadFragment(AdminEvent())
                R.id.bottom_nav_job-> loadFragment(AdminJob())
                R.id.bottom_nav_college -> loadFragment(AdminCollegeProfile())
            }
            true
        }


      //  setupViewPagerWithBottomNavigation()
        return binding.root
    }





    // Function to replace the child fragment
    private fun loadFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, fragment)
            .commit()
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

    override fun onGoToEditProfileClicked() {
        try{
            findNavController().navigate(R.id.action_adminMainPage_to_adminEditProfile)
        }
        catch (e:Exception){
            Log.d("Exception",e.toString())
        }


    }

    override fun onGoToCreateJobClicked() {
        try{
            findNavController().navigate(R.id.action_adminMainPage_to_adminAddJob)
        }
        catch (e:Exception){
            Log.d("Exception",e.toString())
        }
    }



}
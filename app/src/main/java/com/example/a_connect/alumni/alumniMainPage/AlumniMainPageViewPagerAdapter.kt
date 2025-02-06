package com.example.a_connect.alumni.alumniMainPage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.a_connect.alumni.alumniCommunity.AlumniCommunityPage
import com.example.a_connect.alumni.alumniHome.AlumniHomePage

import com.example.a_connect.alumni.alumniJob.AlumniJob
import com.example.a_connect.alumni.alumniProfile.AlumniProfile

import com.example.a_connect.alumni.alumniPost.AlumniPost






class AlumniMainPageViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {

        return when(position){
            0-> AlumniHomePage()
            1->AlumniCommunityPage()
            2->AlumniPost()
            3->AlumniJob()
            else-> AlumniProfile()

        }
    }
}
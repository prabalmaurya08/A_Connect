package com.example.a_connect.admin.adminMainPage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.a_connect.admin.adminCollegeProfile.AdminCollegeProfile
import com.example.a_connect.admin.adminEvent.AdminEvent
import com.example.a_connect.admin.adminHome.AdminHome
import com.example.a_connect.admin.adminJob.AdminJob
import com.example.a_connect.admin.adminNews.AdminNewsAnnouncement
import com.example.a_connect.alumni.alumniCommunity.AluminiCommunityPage
import com.example.a_connect.alumni.alumniEvent.AluminiEvent
import com.example.a_connect.alumni.alumniHome.AluminiHomePage
import com.example.a_connect.alumni.alumniJob.AlumniJob
import com.example.a_connect.alumni.alumniProfile.AlumniProfile

class AdminMainPageViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {

        return when(position){
            0-> AdminHome()
            1-> AdminNewsAnnouncement()
            2-> AdminEvent()
            3-> AdminJob()
            else-> AdminCollegeProfile()

        }
    }
}
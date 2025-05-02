package com.example.a_connect.admin.adminMainPage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.a_connect.admin.adminCollegeProfile.AdminCollegeProfile
import com.example.a_connect.admin.adminEvent.AdminEvent
import com.example.a_connect.admin.adminHome.AdminHome
import com.example.a_connect.admin.adminJob.AdminJob
import com.example.a_connect.admin.adminNews.AdminNewsAnnouncement

class AdminMainPageViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AdminHome()
            1 -> AdminNewsAnnouncement()

            2 -> AdminJob()
            3 -> AdminCollegeProfile()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}

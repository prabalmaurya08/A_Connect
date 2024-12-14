package com.example.a_connect.alumni.alumniMainPage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.a_connect.alumni.alumniCommunity.AluminiCommunityPage
import com.example.a_connect.alumni.alumniHome.AluminiHomePage
import com.example.a_connect.alumni.alumniJob.AlumniJob
import com.example.a_connect.alumni.alumniProfile.AlumniProfile
import com.example.a_connect.alumni.alumniEvent.AluminiEvent


class AlumniMainPageViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {

        return when(position){
            0-> AluminiHomePage()
            1->AluminiCommunityPage()
            2->AluminiEvent()
            3->AlumniJob()
            else-> AlumniProfile()

        }
    }
}
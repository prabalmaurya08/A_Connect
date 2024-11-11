package com.example.a_connect.alumini.aluminiMainPage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.a_connect.alumini.aluminiCommunity.AluminiCommunityPage
import com.example.a_connect.alumini.aluminiHome.AluminiHomePage
import com.example.a_connect.alumini.aluminiJob.AluminiJob
import com.example.a_connect.alumini.aluminiPost.AluminiPost
import com.example.a_connect.alumini.aluminiProfile.AluminiProfile


class AluminiMainPageViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {

        return when(position){
            0-> AluminiHomePage()
            1->AluminiCommunityPage()
            2->AluminiPost()
            3->AluminiJob()
            else-> AluminiProfile()

        }
    }
}
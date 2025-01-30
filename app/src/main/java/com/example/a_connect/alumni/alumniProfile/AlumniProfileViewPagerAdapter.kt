package com.example.a_connect.alumni.alumniProfile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.a_connect.alumni.alumniChat.AluminiChat


class AlumniProfileViewPagerAdapter (fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0->AlumniProfilePost()
            1 -> AlumniProfilePost()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
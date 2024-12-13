package com.example.a_connect.alumini.alumniEvent

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlumniEventsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UpcommingEvents()
            1 ->PastEvents()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
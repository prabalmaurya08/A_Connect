package com.example.a_connect.student.studentEvent

import com.example.a_connect.alumni.alumniEvent.AlumniEventPastEvents
import com.example.a_connect.alumni.alumniEvent.AlumniEventUpcomingEvents
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class StudentEventPagerAdapter  (fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AlumniEventUpcomingEvents()
            1 -> AlumniEventPastEvents()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
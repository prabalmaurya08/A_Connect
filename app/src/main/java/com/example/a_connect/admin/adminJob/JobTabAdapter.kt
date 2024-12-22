package com.example.a_connect.admin.adminJob

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class JobTabAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OngoingJob()
            1 -> ExpiredJob()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}

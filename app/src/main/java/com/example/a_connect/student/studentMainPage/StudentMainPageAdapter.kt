package com.example.a_connect.student.studentMainPage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.example.a_connect.alumni.alumniJob.AlumniJob

import com.example.a_connect.student.studentCommunity.StudentCommunity
import com.example.a_connect.student.studentExplore.StudentExplore
import com.example.a_connect.student.studentHomePage.StudentHomePage

import com.example.a_connect.student.studentProfile.StudentProfile

class StudentMainPageAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {

        return when(position){
            0-> StudentHomePage()
            1-> StudentCommunity()
            2-> StudentExplore()
            3->AlumniJob()
            else-> StudentProfile()

        }
    }
}
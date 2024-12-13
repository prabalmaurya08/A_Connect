package com.example.a_connect.student.studentJob

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a_connect.R
import com.example.a_connect.databinding.FragmentStudentJobBinding


class StudentJob : Fragment() {
    private lateinit var binding: FragmentStudentJobBinding
    private lateinit var adapterClass: StudentJobAdapterClass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentStudentJobBinding.inflate(layoutInflater)
        setupCard()
        return binding.root
    }
    private fun setupCard(){
        val cardItem= listOf(
            studentJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025"),
            studentJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025"),
            studentJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025"),
            studentJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025"),
            studentJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025"),
            studentJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025"),
            studentJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025"),
            studentJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025"),
            studentJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025"),
            studentJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025"),
            studentJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025")

        )
        adapterClass=StudentJobAdapterClass(cardItem)
        binding.studentJobRecyclerview.apply {
            layoutManager=LinearLayoutManager(context)
            adapter=adapterClass
        }
    }


}
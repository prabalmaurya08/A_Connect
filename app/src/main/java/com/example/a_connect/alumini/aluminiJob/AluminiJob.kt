package com.example.a_connect.alumini.aluminiJob

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a_connect.R
import com.example.a_connect.databinding.FragmentAluminiJobBinding


class AluminiJob : Fragment() {
    private lateinit var binding: FragmentAluminiJobBinding
    private lateinit var adapterClass: alumniJobAdapterClass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAluminiJobBinding.inflate(layoutInflater)
        return binding.root
    }
    private fun setupCard(){
        val cardItem= listOf(
            alumniJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025"),
            alumniJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025"),
            alumniJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025"),
            alumniJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025"),
            alumniJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025"),
            alumniJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025"),
            alumniJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025"),
            alumniJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025"),
            alumniJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025"),
            alumniJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025"),
            alumniJobDataitem(R.drawable.google,"Software Developer 2","Google","lucknow,U.P","4 days Ago","06 february 2025")

        )
        adapterClass=alumniJobAdapterClass(cardItem)
        binding.alumniJobRecyclerview.apply {
            layoutManager=LinearLayoutManager(context)
            adapter=adapterClass
        }
    }


}
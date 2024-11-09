package com.example.a_connect.alumini

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a_connect.R
import com.example.a_connect.databinding.FragmentAluminiMainPageBinding


class AluminiMainPage : Fragment() {
    private lateinit var binding: FragmentAluminiMainPageBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAluminiMainPageBinding.inflate(layoutInflater)
        return binding.root
    }




}
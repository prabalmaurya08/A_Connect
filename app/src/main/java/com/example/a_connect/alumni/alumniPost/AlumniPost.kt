package com.example.a_connect.alumni.alumniPost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a_connect.R
import com.example.a_connect.databinding.FragmentAlumniPostBinding


class AlumniPost : Fragment() {
    private lateinit var binding: FragmentAlumniPostBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAlumniPostBinding.inflate(inflater,container,false)
        return binding.root
    }

}
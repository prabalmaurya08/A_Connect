package com.example.a_connect.alumni.alumniChat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a_connect.databinding.FragmentAlumniChatBinding


class AlumniChat : Fragment() {
    private lateinit var binding: FragmentAlumniChatBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentAlumniChatBinding.inflate(layoutInflater)
        return binding.root
    }

}
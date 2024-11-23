package com.example.a_connect.alumini.aluminiChat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a_connect.R
import com.example.a_connect.databinding.FragmentAluminiChatBinding


class AluminiChat : Fragment() {
    private lateinit var binding: FragmentAluminiChatBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAluminiChatBinding.inflate(layoutInflater)
        return binding.root
    }

}
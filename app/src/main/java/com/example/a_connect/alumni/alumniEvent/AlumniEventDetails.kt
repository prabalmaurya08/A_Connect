package com.example.a_connect.alumni.alumniEvent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a_connect.databinding.FragmentAlumniEventBinding

class AlumniEventDetails : Fragment() {

    private var _binding: FragmentAlumniEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAlumniEventBinding.inflate(inflater, container, false)

        return binding.root
    }

}
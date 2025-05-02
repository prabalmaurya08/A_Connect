package com.example.a_connect.alumni.alumniEvent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a_connect.R
import com.example.a_connect.databinding.FragmentAlumniEventPastEventBinding


class AlumniEventPastEvents : Fragment() {

    private var _binding: FragmentAlumniEventPastEventBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlumniEventPastEventBinding.inflate(inflater, container, false)
     
        return binding.root
    }

}
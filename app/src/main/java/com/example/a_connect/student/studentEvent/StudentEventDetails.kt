package com.example.a_connect.student.studentEvent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a_connect.databinding.FragmentStudentEventBinding


class StudentEventDetails : Fragment() {
    private var _binding: FragmentStudentEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStudentEventBinding.inflate(inflater, container, false)

        return binding.root
    }
}
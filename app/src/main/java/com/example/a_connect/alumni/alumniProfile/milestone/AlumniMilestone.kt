package com.example.a_connect.alumni.alumniProfile.milestone

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a_connect.R
import com.example.a_connect.SharedPreferencesHelper
import com.example.a_connect.UserSessionManager
import com.example.a_connect.databinding.FragmentAlumniMilestoneBinding
import com.example.a_connect.student.studentProfile.milestone.StudentMilestoneAdapter
import com.example.a_connect.student.studentProfile.milestone.StudentMilestoneViewModel


class AlumniMilestone : Fragment() {
    private var _binding: FragmentAlumniMilestoneBinding? = null
    private val binding get() = _binding!!
    //private lateinit var userSessionManager: UserSessionManager
    private lateinit var milestoneViewModel: StudentMilestoneViewModel
    private lateinit var timelineAdapter: StudentMilestoneAdapter
    private lateinit var currentUserEmail: String

    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAlumniMilestoneBinding.inflate(inflater, container, false)
        milestoneViewModel = ViewModelProvider(this)[StudentMilestoneViewModel::class.java]
       // userSessionManager = UserSessionManager(requireContext())
        val email = SharedPreferencesHelper.getCurrentUserEmail()
        if (email == null) {
            Log.e("AlumniMilestone", "User email is null. Cannot fetch milestones.")
            return binding.root
        }
        currentUserEmail = email


        milestoneViewModel = ViewModelProvider(this)[StudentMilestoneViewModel::class.java]
        recyclerView=binding.timelineRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Initialize RecyclerView
        timelineAdapter = StudentMilestoneAdapter(emptyList())
        recyclerView.adapter = timelineAdapter

        // Observe milestones from ViewModel
        milestoneViewModel.milestones.observe(viewLifecycleOwner, Observer { milestones ->
            if (milestones.isNotEmpty()) {
                timelineAdapter = StudentMilestoneAdapter(milestones)
                recyclerView.adapter = timelineAdapter
            } else {
                Log.d("Milestones", "No milestones available")
            }
        })

        // Fetch milestones from ViewModel
       // milestoneViewModel.getAlumniMilestones(currentUserEmail)
        milestoneViewModel.getAlumniMilestones(currentUserEmail)
        return binding.root

    }

}

package com.example.a_connect.student.studentProfile

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.a_connect.R
import com.example.a_connect.SharedPreferencesHelper
import com.example.a_connect.UserSessionManager

import com.example.a_connect.databinding.FragmentStudentProfileBinding
import com.example.a_connect.student.studentProfile.milestone.StudentMilestoneAdapter
import com.example.a_connect.student.studentProfile.milestone.StudentMilestoneViewModel
import com.example.a_connect.student.studentProfile.mvvm.StudentEditProfileViewModelFactory
import com.example.a_connect.student.studentProfile.mvvm.StudentProfileRepository
import com.example.a_connect.student.studentProfile.mvvm.StudentProfileViewmodel
import kotlin.getValue


class StudentProfile : Fragment() {
    private var _binding: FragmentStudentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val viewModel: StudentProfileViewmodel by viewModels {
        StudentEditProfileViewModelFactory(StudentProfileRepository())
    }
    private lateinit var sessionManager: UserSessionManager
    private lateinit var currentUserEmail: String
    private lateinit var recyclerView: RecyclerView

    private lateinit var milestoneViewModel: StudentMilestoneViewModel
    private lateinit var timelineAdapter: StudentMilestoneAdapter

    private var listener: OnStudentProfileItemClicked? = null



    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {

            is OnStudentProfileItemClicked -> {
                listener=context


            }



            else -> {
                throw ClassCastException("$context must implement OnSignupClickListener")
            }
        }
    }
    interface OnStudentProfileItemClicked {
        fun onStudentEditProfileClicked()
        fun onUniqueIdClicked()
        fun onShareProfileClicked()
        fun onAddMilestoneClicked()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStudentProfileBinding.inflate(inflater, container, false)
        milestoneViewModel = ViewModelProvider(this)[StudentMilestoneViewModel::class.java]
        recyclerView=binding.timelineRecyclerView

        swipeRefreshLayout = binding.swipeRefreshLayout

        sessionManager = UserSessionManager(requireContext())

        currentUserEmail=sessionManager.getCurrentUserEmail().toString()
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
        milestoneViewModel.getMilestones(currentUserEmail)

        swipeRefreshLayout.setOnRefreshListener {

//            viewModel.loadUserPosts(currentUserEmail) // Refresh posts
            swipeRefreshLayout.isRefreshing = false // Stop refresh animation
        }
        binding.addMilestone.setOnClickListener {
            listener?.onAddMilestoneClicked()
        }
        binding.studentProfileCircleImageView.setOnClickListener {
            listener?.onUniqueIdClicked()

        }


        binding.studentEditProfileButton.setOnClickListener {
            listener?.onStudentEditProfileClicked()
        }
        //for data to load
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.fetchUserProfile(currentUserEmail)
        // Observe the profilePic LiveData and update the image view if it changes
        viewModel.profilePic.observe(viewLifecycleOwner, Observer { profilePicUrl ->
            profilePicUrl?.let {
                // Load image from URL (or locally stored URI) into the ImageView
                // Assuming you are using an image loading library like Glide or Picasso
                Glide.with(requireContext())
                    .load(profilePicUrl)  // If the URL is stored in Firestore, this should work
                    .into(binding.studentProfileCircleImageView)
            }
        })

        binding.btnLogout.setOnClickListener(){
            logout()
        }




        return binding.root
    }

    private fun logout() {

            val sessionManager = UserSessionManager(requireContext())
            sessionManager.logout()

            findNavController().navigate(R.id.action_studentMainPage_to_mainLogin, null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.mainLogin, true)
                    .build())
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
        }
}





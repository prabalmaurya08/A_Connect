package com.example.a_connect.admin.adminCollegeProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.a_connect.R
import com.example.a_connect.admin.adminCollegeProfile.mvvm.CollegeEditProfileViewModel
import com.example.a_connect.admin.adminCollegeProfile.mvvm.CollegeProfileRepository
import com.example.a_connect.admin.adminCollegeProfile.mvvm.EditProfileViewModelFactory
import com.example.a_connect.databinding.FragmentAdminEditProfileBinding


class AdminEditProfile : Fragment() {
    private lateinit var binding: FragmentAdminEditProfileBinding

    private val repository = CollegeProfileRepository()
    private val viewModel: CollegeEditProfileViewModel by viewModels {
        EditProfileViewModelFactory(repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_admin_edit_profile,container,false)
        binding.lifecycleOwner=viewLifecycleOwner
        binding.viewModel=viewModel
        val collegeId = "collegeId123" // Replace with actual ID
        viewModel.loadProfileData(collegeId)
        // Save button click listener
        binding.saveProfile.setOnClickListener {
            viewModel.updateProfile(collegeId)
        }

        viewModel.isProfileUpdated.observe(viewLifecycleOwner) { isUpdated ->
            if (isUpdated) {
                // Navigate back or show success message
                requireActivity().onBackPressed()
            } else {
                // Handle failure
            }
        }

        return binding.root
    }


}
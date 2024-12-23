package com.example.a_connect.admin.adminCollegeProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.a_connect.R
import com.example.a_connect.admin.adminCollegeProfile.mvvm.CollegeProfileRepository
import com.example.a_connect.admin.adminCollegeProfile.mvvm.CollegeProfileViewModel
import com.example.a_connect.admin.adminCollegeProfile.mvvm.EditProfileViewModelFactory
import com.example.a_connect.databinding.FragmentAdminEditProfileBinding


class AdminEditProfile : Fragment() {
    private lateinit var binding: FragmentAdminEditProfileBinding

    private val repository = CollegeProfileRepository()
    private val viewModel: CollegeProfileViewModel by viewModels {
        EditProfileViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_admin_edit_profile,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        // Set the collegeId (Replace this with actual ID in your application)
        val collegeId = "collegeId123" // Replace with actual collegeId
        viewModel.loadProfileData(collegeId)

        // Save button click listener
        binding.saveProfile.setOnClickListener {
            // Since two-way binding is set up, the updated values are already in the ViewModel
            viewModel.updateProfile(collegeId)

            // Save the graduation years and college names, using the values bound in the ViewModel
            viewModel.saveGraduationYearsAndCollegeNames(collegeId)
        }

        // Observe if the profile update is successful
        viewModel.isProfileUpdated.observe(viewLifecycleOwner) { isUpdated ->
            if (isUpdated) {
                // Navigate back or show success message
                requireActivity().onBackPressed()
            } else {
                // Handle failure (e.g., show error message)
            }
        }

        return binding.root
    }
}


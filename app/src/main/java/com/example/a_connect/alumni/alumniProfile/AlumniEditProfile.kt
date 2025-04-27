package com.example.a_connect.alumni.alumniProfile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.a_connect.R
import com.example.a_connect.SharedPreferencesHelper
import com.example.a_connect.alumni.alumniProfile.mvvm.AlumniEditProfileViewModelFactory
import com.example.a_connect.alumni.alumniProfile.mvvm.AlumniProfileRepository
import com.example.a_connect.alumni.alumniProfile.mvvm.AlumniProfileViewModel
import com.example.a_connect.databinding.FragmentAlumniEditProfileBinding

class AlumniEditProfile : Fragment() {
    private lateinit var binding: FragmentAlumniEditProfileBinding
    private val viewModel: AlumniProfileViewModel by viewModels {
        AlumniEditProfileViewModelFactory(AlumniProfileRepository())
    }
    private lateinit var currentUserEmail: String
    private var profileImageUri: Uri? = null

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            profileImageUri = it
            binding.editProfileCircleImageView.setImageURI(uri)  // Assuming you have an ImageView to show the selected image
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlumniEditProfileBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        currentUserEmail = SharedPreferencesHelper.getCurrentUserEmail() ?: return binding.root

        setupUI()
        observeViewModel()

        // Load the user profile data
        viewModel.fetchUserProfile(currentUserEmail)

        // Observe the profilePic LiveData and update the image view if it changes
        viewModel.profilePic.observe(viewLifecycleOwner, Observer { profilePicUrl ->
            profilePicUrl?.let {
                // Load image from URL (or locally stored URI) into the ImageView
                // Assuming you are using an image loading library like Glide or Picasso
                Glide.with(requireContext())
                    .load(profilePicUrl)  // If the URL is stored in Firestore, this should work
                    .into(binding.editProfileCircleImageView)
            }
        })

        return binding.root
    }

    private fun setupUI() {
        // Setup Degree Specialization Spinner
        val degreeOptions = arrayOf("Computer Science", "Mechanical", "Electrical", "Civil", "Electronics", "Others")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, degreeOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.alumniDegreeObtainInput.adapter = adapter

        // Profile picture click listener
        binding.editProfileCircleImageView.setOnClickListener {
            imagePickerLauncher.launch("image/*")  // Trigger image picker for the user to select a profile picture
        }

        // Save Button Click Listener
        binding.Save.setOnClickListener {
            if (validateInput()) {
                saveUserProfile()
            }
        }
    }

    private fun observeViewModel() {
        // Show/Hide ProgressBar while fetching/updating
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.Save.text = "Updating..."
                binding.Save.isEnabled = false  // Disable button to prevent multiple clicks
            } else {
                binding.Save.text = "Save"
                binding.Save.isEnabled = true
            }
        })

        // Observe Update Success Message
        viewModel.updateSuccess.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                Toast.makeText(requireContext(), "Profile Updated Successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Failed to Update Profile", Toast.LENGTH_SHORT).show()
            }
        })

        // Load previously saved Gender
        viewModel.gender.observe(viewLifecycleOwner) { gender ->
            when (gender) {
                "Male" -> binding.genderRadioGroup.check(R.id.rbMale)
                "Female" -> binding.genderRadioGroup.check(R.id.rbFemale)
                "Others" -> binding.genderRadioGroup.check(R.id.rbOthers)
            }
        }
    }

    private fun validateInput(): Boolean {
        if (binding.editAlumniName.text.toString().trim().isEmpty()) {
            binding.editAlumniName.error = "Name is required"
            return false
        }
        if (binding.editAlumniBio.text.toString().trim().isEmpty()) {
            binding.editAlumniBio.error = "Bio is required"
            return false
        }
        return true
    }

    private fun saveUserProfile() {
        // Get selected Gender from RadioGroup
        val selectedGenderId = binding.genderRadioGroup.checkedRadioButtonId
        val selectedGender = when (selectedGenderId) {
            R.id.rbMale -> "Male"
            R.id.rbFemale -> "Female"
            else -> "Others"
        }
        viewModel.gender.value = selectedGender

        // Get selected Degree Specialization from Spinner
        viewModel.degreeSpecialization.value = binding.alumniDegreeObtainInput.selectedItem.toString()

        // Update user profile on Firestore (including the new profile picture)
        viewModel.updateUserProfile(currentUserEmail, profileImageUri)
    }
}


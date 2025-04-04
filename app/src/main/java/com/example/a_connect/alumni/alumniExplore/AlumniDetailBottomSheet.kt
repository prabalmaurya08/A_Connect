package com.example.a_connect.alumni.alumniExplore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.a_connect.R
import com.example.a_connect.databinding.FragmentAlumniDetailBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AlumniDetailBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAlumniDetailBottomSheetBinding

    // Define keys to get data from the Bundle
    companion object {
        private const val NAME_KEY = "name"
        private const val PROFILE_PIC_KEY = "profile_pic"
        private const val PHONE_KEY = "phone"

        // Pass data via Bundle (using simple types)
        fun newInstance(name: String, profilePic: String, phone: String?): AlumniDetailBottomSheet {
            val args = Bundle().apply {
                putString(NAME_KEY, name)
                putString(PROFILE_PIC_KEY, profilePic)
                putString(PHONE_KEY, phone)
            }
            return AlumniDetailBottomSheet().apply { arguments = args }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAlumniDetailBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve data from the Bundle
        val name = arguments?.getString(NAME_KEY)
        val profilePic = arguments?.getString(PROFILE_PIC_KEY)
        val phone = arguments?.getString(PHONE_KEY)

        // Load data into views
        name?.let { binding.alumniName.text = it }
        profilePic?.let {
            Glide.with(this)
                .load(it)
                .circleCrop() // To make the profile picture round
                .into(binding.profilePic)
        }

        // Set up the contact button
        binding.contactButton.setOnClickListener {
            val contactInfo = "Phone: ${phone ?: "Not available"}"
            Toast.makeText(requireContext(), contactInfo, Toast.LENGTH_SHORT).show()
        }

        // Set up the "View Profile" button
        binding.viewProfileButton.setOnClickListener {
            // Handle "View Profile" button action (you can navigate to another screen if needed)
            Toast.makeText(requireContext(), "Viewing full profile", Toast.LENGTH_SHORT).show()
        }
    }
}

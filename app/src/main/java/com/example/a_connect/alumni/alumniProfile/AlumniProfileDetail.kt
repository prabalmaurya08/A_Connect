package com.example.a_connect.alumni.alumniProfile

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.a_connect.R
import com.example.a_connect.alumni.alumniProfile.mvvm2.AlumniProfileDetailDataClass
import com.example.a_connect.alumni.alumniProfile.mvvm2.AlumniProfileDetailRepository
import com.example.a_connect.alumni.alumniProfile.mvvm2.AlumniProfileDetailViewModel
import com.example.a_connect.alumni.alumniProfile.mvvm2.AlumniProfileDetailViewModelFactory
import com.example.a_connect.databinding.FragmentAlumniProfileDetailBinding
import androidx.core.net.toUri


class AlumniProfileDetail : Fragment() {
    private var _binding: FragmentAlumniProfileDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AlumniProfileDetailViewModel
    private lateinit var loadingDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAlumniProfileDetailBinding.inflate(inflater, container, false)
initLoadingDialog()
        // Get email passed via arguments
        val email = arguments?.getString("email")

        // Log the email value
        Log.d("AlumniProfileDetail", "Received Email: $email")

        if (email.isNullOrEmpty()) {
            Log.e("AlumniProfileDetail", "No email received or email is empty.")
        } else {
            Log.d("AlumniProfileDetail", "Valid email received: $email")
        }

        // Initialize ViewModel
        val repository = AlumniProfileDetailRepository()
        val factory = AlumniProfileDetailViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[AlumniProfileDetailViewModel::class.java]

        // Observe data
        viewModel.profileData.observe(viewLifecycleOwner) { profile ->
            Log.d("AlumniProfileDetail", "Profile data received: $profile")
            if (profile != null) {
                bindProfileData(profile)
                setupSocialIcons(profile)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }

        // Load profile
        if (!email.isNullOrEmpty()) {
            viewModel.loadAlumniProfile(email)
        }

        return binding.root
    }

    private fun bindProfileData(profile: AlumniProfileDetailDataClass) {
        binding.nameText.text = profile.name
        binding.headlineText.text = profile.headline
        binding.bioText.text = profile.bio
        binding.collegeNameText.text = profile.collegeName
        binding.graduationYearText.text = profile.graduationYear.toString()

        // Load profile image
        Glide.with(requireContext())
            .load(profile.profilePic)
            .placeholder(R.drawable.ic_media)
            .into(binding.profileImageView)

//        // Optional fields
//        binding.phoneLayout.visibility = if (profile.phoneNumber.isNullOrEmpty()) View.GONE else View.VISIBLE
//        binding.phoneTextView.text = profile.phoneNumber
//
//        binding.websiteLayout.visibility = if (profile.websiteLink.isNullOrEmpty()) View.GONE else View.VISIBLE
//        binding.websiteTextView.text = profile.websiteLink


    }

    private fun setupSocialIcons(profile: AlumniProfileDetailDataClass) {
        // LinkedIn
        if (!profile.linkedinUrl.isNullOrEmpty()) {
            binding.linkedinIcon.visibility = View.VISIBLE
            binding.linkedinIcon.setOnClickListener {
                openUrl(profile.linkedinUrl!!)
            }
        } else {
            binding.linkedinIcon.visibility = View.GONE
        }

        // Instagram
        if (!profile.instagramUrl.isNullOrEmpty()) {
            binding.instagramIcon.visibility = View.VISIBLE
            binding.instagramIcon.setOnClickListener {
                openUrl(profile.instagramUrl!!)
            }
        } else {
            binding.instagramIcon.visibility = View.GONE
        }

        // Gmail (open mail client)
        if (!profile.gmailUrl.isNullOrEmpty()) {
            binding.gmailIcon.visibility = View.VISIBLE
            binding.gmailIcon.setOnClickListener {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:${profile.gmailUrl}".toUri()
                }
                startActivity(intent)
            }
        } else {
            binding.gmailIcon.visibility = View.GONE
        }

        // Threads
        if (!profile.threadsUrl.isNullOrEmpty()) {
            binding.threadsIcon.visibility = View.VISIBLE
            binding.threadsIcon.setOnClickListener {
                openUrl(profile.threadsUrl!!)
            }
        } else {
            binding.threadsIcon.visibility = View.GONE
        }
    }
    // Initialize the loading dialog with Lottie animation
    private fun initLoadingDialog() {
        loadingDialog = Dialog(requireContext())
        loadingDialog.setContentView(R.layout.dialog_loading)
        loadingDialog.setCancelable(false)  // Prevent user interaction during loading
    }

    // Helper to open URL
    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "No browser found to open the link", Toast.LENGTH_SHORT).show()
        }
    }




}
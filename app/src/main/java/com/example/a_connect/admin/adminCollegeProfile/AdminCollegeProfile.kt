package com.example.a_connect.admin.adminCollegeProfile

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import com.bumptech.glide.Glide
import com.example.a_connect.R
import com.example.a_connect.admin.adminCollegeProfile.mvvm.CollegeProfileRepository
import com.example.a_connect.admin.adminCollegeProfile.mvvm.CollegeProfileViewModel
import com.example.a_connect.admin.adminCollegeProfile.mvvm.EditProfileViewModelFactory
import com.example.a_connect.databinding.FragmentAdminCollegeProfileBinding


class AdminCollegeProfile : Fragment() {

    private lateinit var binding: FragmentAdminCollegeProfileBinding
    private val IMAGE_PICK_REQUEST = 1003
    private var imageUri: Uri? = null
    private var loadingDialog: Dialog? = null
    private var pb:ProgressBar?=null
    private val collegeId = "collegeId123" // Replace with dynamic ID if necessary
    private var isProfileLoaded = false
    private val repository = CollegeProfileRepository()
    private val viewModel: CollegeProfileViewModel by viewModels {
        EditProfileViewModelFactory(repository)
    }

    private var listener: OnGoToEditProfileClickListener? = null

    interface OnGoToEditProfileClickListener {
        fun onGoToEditProfileClicked()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)


        if (parentFragment is OnGoToEditProfileClickListener) {
            listener = parentFragment as OnGoToEditProfileClickListener
        } else {
            throw RuntimeException("$context must implement OnViewPdfButtonClickListener")
        }
    }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_college_profile, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        // Load College Profile Data
        viewModel.loadProfileData(collegeId)

        setupUI()
        setupObservers()
        observeImageLoadingStatus()
        return binding.root
    }

    private fun setupUI() {
        // Add Image
        binding.addimage.setOnClickListener {
            openImagePicker()
        }

        // Delete Image
        binding.deleteimage.setOnClickListener {
            viewModel.deleteImage(collegeId)
            Toast.makeText(requireContext(), "Image deleted successfully", Toast.LENGTH_SHORT).show()
        }

        binding.goToEditProfile.setOnClickListener {
            listener?.onGoToEditProfileClicked()
        }

        // Import Excel Buttons
        binding.importAlumniExcelData.setOnClickListener {
            selectExcelFile(PICK_FILE_REQUEST_CODE_ALUMNI)
        }

        binding.importStudentExcelData.setOnClickListener {
            selectExcelFile(PICK_FILE_REQUEST_CODE_STUDENT)
        }

        setupLoadingDialog()
        setupClickableLinks()
    }

    // Observe the image loading status to skip loading when already done
    private fun observeImageLoadingStatus() {
        viewModel.isImageLoaded.observe(viewLifecycleOwner, Observer { isLoaded ->
            if (isLoaded && !isProfileLoaded) {
                // If the image is already loaded, do not show loading dialog again
                isProfileLoaded = true
                // Optionally, hide loading indicator here
            } else {
                // Show loading dialog if the image is not yet loaded
                if (!isProfileLoaded) {
                    // Show loading dialog or indicator
                }
            }
        })
    }

    private fun setupLoadingDialog() {
        loadingDialog = Dialog(requireContext()).apply {
            setContentView(R.layout.progress_bar_dialogbox) // Replace with your custom dialog layout
            setCancelable(false) // Prevent dialog dismissal by back button
            window?.setBackgroundDrawableResource(android.R.color.transparent) // Transparent background
        }
    }

    private fun showLoadingDialog(message: String = "Loading...") {
        loadingDialog?.findViewById<TextView>(R.id.loadingMessage)?.text = message
        loadingDialog?.show()
        pb=loadingDialog?.findViewById<ProgressBar>(R.id.progressBar)
    }

    private fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    private fun setupObservers() {
        // Observe loading state to show/hide dialog
        viewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                showLoadingDialog("Loading your image...")
            } else {
                dismissLoadingDialog()
            }
        }

        // Observe image URL to update UI when the upload is complete
        viewModel.imageUrl.observe(viewLifecycleOwner) { imageUrl ->
            if (!imageUrl.isNullOrEmpty()) {
                // Load image using Glide
                Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.alumni_job_detail_bg) // Placeholder image
                    .error(R.drawable.alumni_job_detail_bg)       // Error fallback image
                    .into(binding.backgroundImage)
            } else {
                // Set default image if no URL is found
                binding.backgroundImage.setImageResource(R.drawable.alumni_job_detail_bg)
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_REQUEST)
    }

    private fun selectExcelFile(requestCode: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        startActivityForResult(Intent.createChooser(intent, "Select Excel File"), requestCode)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_PICK_REQUEST -> {
                    imageUri = data?.data
                    imageUri?.let { uri ->
                        viewModel.uploadImage(collegeId, uri)
                        Toast.makeText(requireContext(), "Image uploading...", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissLoadingDialog() // Ensure dialog is dismissed when fragment is destroyed
    }

    private fun setupClickableLinks() {
        binding.linkedinConstraint.setOnClickListener {
            openLink(viewModel.linkedinUrl.value)
        }

        binding.gmailConstraint.setOnClickListener {
            openLink("mailto:${viewModel.gmailUrl.value}")
        }

        binding.instaConstraint.setOnClickListener {
            openLink(viewModel.instagramUrl.value)
        }

        binding.threadConstraint.setOnClickListener {
            openLink(viewModel.threadsUrl.value)
        }
    }

    private fun openLink(url: String?) {
        url?.let {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(intent)
        } ?: Toast.makeText(requireContext(), "Invalid URL", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val PICK_FILE_REQUEST_CODE_ALUMNI = 1001
        private const val PICK_FILE_REQUEST_CODE_STUDENT = 1002
    }
}

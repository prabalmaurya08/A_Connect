package com.example.a_connect.alumni.alumniPost

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.example.a_connect.R

import com.example.a_connect.alumni.alumniPost.mvvm.AlumniPostViewmodel
import com.example.a_connect.databinding.FragmentAlumniPostBinding

class AlumniPost : Fragment() {
    private lateinit var binding: FragmentAlumniPostBinding
    private lateinit var postViewModel: AlumniPostViewmodel
    private val pickImageRequestCode = 1001
    private lateinit var loadingDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAlumniPostBinding.inflate(inflater, container, false)
        postViewModel = ViewModelProvider(this)[AlumniPostViewmodel::class.java]

        // Initialize the loading dialog with Lottie animation
        initLoadingDialog()

        // Observing image URI
        postViewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                binding.previewImage.setImageURI(it)
                binding.previewCard.visibility=View.VISIBLE
                binding.previewImage.visibility=View.VISIBLE

            }
        }

        // Observing post creation status
        postViewModel.postStatus.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(context, "Post created successfully", Toast.LENGTH_SHORT).show()
                binding.description.text.clear()
                binding.previewImage.setImageDrawable(null) // Clear image preview
                postViewModel.setImageUri(null) // Reset image URI
            } else {
                Toast.makeText(context, "Failed to create post", Toast.LENGTH_SHORT).show()
            }
        }

        // Observing uploading state
        postViewModel.isUploading.observe(viewLifecycleOwner) { isUploading ->
            binding.createPost.isEnabled = !isUploading
            if (isUploading) {
                binding.createPost.text = "Posting..."
            } else {
                binding.createPost.text = "Create Post"
            }
        }

        // Media picker to select an image
        binding.mediaCard.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, pickImageRequestCode)
        }

        // Handle post creation
        binding.createPost.setOnClickListener {
            val description = binding.description.text.toString().trim()

            if (description.isEmpty()) {
                Toast.makeText(context, "Description cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (postViewModel.imageUri.value == null) {
                Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            postViewModel.createPost(description)
        }

        // Set up the "Generate with AI" button click listener
        rewriteWithAiSetup()

        return binding.root
    }

    // Handle result from the image picker
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == pickImageRequestCode) {
            data?.data?.let { uri ->
                postViewModel.setImageUri(uri)  // Set the selected image URI in ViewModel
            }
        }
    }

    // Initialize the loading dialog with Lottie animation
    private fun initLoadingDialog() {
        loadingDialog = Dialog(requireContext())
        loadingDialog.setContentView(R.layout.dialog_loading)
        loadingDialog.setCancelable(false)  // Prevent user interaction during loading
    }

    // Set up the logic for "Generate with AI" TextView
    private fun rewriteWithAiSetup() {
        binding.generateAi.setOnClickListener {
            val description = binding.description.text.toString().trim()

            if (description.isEmpty()) {
                Toast.makeText(
                    context,
                    "Please enter a description before generating AI text",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Show loading dialog with animation while waiting for AI result
            loadingDialog.show()

            // Call ViewModel to generate AI description
            postViewModel.generateAiDescription(description)
        }

        // Observe AI-generated description from ViewModel
        postViewModel.aiGeneratedDescription.observe(viewLifecycleOwner) { generatedDescription ->
            // Update EditText with generated description
            binding.description.setText(generatedDescription)
            loadingDialog.dismiss()  // Dismiss the loading dialog after AI generation
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding.description.text.clear()
        binding.previewImage.setImageDrawable(null) // Clear image preview
        postViewModel.setImageUri(null)
    }
}

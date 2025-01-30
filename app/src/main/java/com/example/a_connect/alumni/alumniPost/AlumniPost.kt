package com.example.a_connect.alumni.alumniPost

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.a_connect.R
import com.example.a_connect.alumni.alumniPost.mvvm.AlumniPostViewmodel
import com.example.a_connect.databinding.FragmentAlumniPostBinding


class AlumniPost : Fragment() {
    private lateinit var binding: FragmentAlumniPostBinding

    private lateinit var postViewModel: AlumniPostViewmodel
    private val pickImageRequestCode = 1001

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAlumniPostBinding.inflate(inflater,container,false)

        postViewModel = ViewModelProvider(this)[AlumniPostViewmodel::class.java]
// Observing image URI
        postViewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            uri?.let { binding.previewImage.setImageURI(it) }
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

        binding.media.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, pickImageRequestCode)
        }

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

        return binding.root
    }
  //   Handle result from the image picker
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == pickImageRequestCode) {
            data?.data?.let { uri ->
                postViewModel.setImageUri(uri)  // Set the selected image URI in ViewModel
            }
        }
    }



}
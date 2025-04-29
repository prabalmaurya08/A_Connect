package com.example.a_connect.admin.adminNews

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.a_connect.R
import com.example.a_connect.admin.adminNews.mvvm.NewsDataClass
import com.example.a_connect.admin.adminNews.mvvm.NewsRepository
import com.example.a_connect.admin.adminNews.mvvm.NewsViewModel
import com.example.a_connect.admin.adminNews.mvvm.NewsViewModelFactory
import com.example.a_connect.databinding.FragmentAdminAddNewsAnnouncementBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class AdminAddNewsAnnouncement : Fragment() {

    private var _binding: FragmentAdminAddNewsAnnouncementBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by viewModels {
        NewsViewModelFactory(NewsRepository())
    }

    private lateinit var headlineEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var selectHeadlineImageBtn: FrameLayout
    private lateinit var selectImageBtn: FrameLayout
    private lateinit var addNewsBtn: FloatingActionButton
   // private lateinit var progressBar: ProgressBar
    private lateinit var headlineImageView: ImageView
    private lateinit var imageView: ImageView
    private lateinit var loadingDialog: Dialog

    private var headlinePhotoUri: Uri? = null
    private var photoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminAddNewsAnnouncementBinding.inflate(inflater, container, false)
        initLoadingDialog()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        headlineEditText = view.findViewById(R.id.edit_news_heading)
        contentEditText = view.findViewById(R.id.edit_news_description)
        selectHeadlineImageBtn = view.findViewById(R.id.selectHeadlineImageBtn)
        selectImageBtn = view.findViewById(R.id.selectImageBtn)
        addNewsBtn = view.findViewById(R.id.add_new_button)
      //  progressBar = view.findViewById(R.id.progressBar)
        headlineImageView = view.findViewById(R.id.selected_headline_image)
        imageView = view.findViewById(R.id.selected_image)

        // Initially hide ImageViews
        headlineImageView.visibility = View.GONE
        imageView.visibility = View.GONE

        selectHeadlineImageBtn.setOnClickListener { selectImage(HEADLINE_IMAGE_REQUEST_CODE) }
        selectImageBtn.setOnClickListener { selectImage(IMAGE_REQUEST_CODE) }
        addNewsBtn.setOnClickListener { addNews() }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.loadingState.collect { isLoading ->
                        if (isLoading) {
                            loadingDialog.show()
                        } else {
                            loadingDialog.dismiss()
                            Toast.makeText(requireContext(), "News Added Successfully", Toast.LENGTH_SHORT).show()
                            headlineEditText.text.clear()
                            contentEditText.text.clear()
                            headlinePhotoUri = null
                            photoUri = null
                            headlineImageView.visibility = View.GONE
                            imageView.visibility = View.GONE
                            binding.tv1.visibility=View.VISIBLE
                            binding.tv2.visibility=View.VISIBLE
                        }
                     //   progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                    }
                }
                launch {
                    viewModel.errorState.collect { error ->
                        error?.let {
                            Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
                            viewModel.clearError()
                        }
                    }
                }
            }
        }
    }

    private fun selectImage(requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                when (requestCode) {
                    HEADLINE_IMAGE_REQUEST_CODE -> {
                        headlinePhotoUri = uri
                        headlineImageView.setImageURI(uri)
                        headlineImageView.visibility = View.VISIBLE
                        binding.tv1.visibility=View.GONE
                        // Make visible after selection
                        Toast.makeText(requireContext(), "Headline Image Selected", Toast.LENGTH_SHORT).show()
                    }
                    IMAGE_REQUEST_CODE -> {
                        photoUri = uri
                        imageView.setImageURI(uri)
                        imageView.visibility = View.VISIBLE  // Make visible after selection
                        binding.tv2.visibility=View.GONE
                        Toast.makeText(requireContext(), "News Image Selected", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun addNews() {
        val headline = headlineEditText.text.toString().trim()
        val content = contentEditText.text.toString().trim()

        if (headline.isBlank() || content.isBlank()) {
            Toast.makeText(requireContext(), "Headline and Content are required", Toast.LENGTH_SHORT).show()
            return
        }

        val headlinePhotoBytes = headlinePhotoUri?.let { uriToByteArray(it) }
        val photoBytes = photoUri?.let { uriToByteArray(it) }

        val news = NewsDataClass(heading = headline, description = content)

        Toast.makeText(requireContext(), "Saving News...", Toast.LENGTH_SHORT).show()
        Log.d("AdminAddNews", "Saving News: $news")  // Debug Log

        viewModel.saveNews(news, headlinePhotoBytes, photoBytes)
    }
    // Initialize the loading dialog with Lottie animation
    private fun initLoadingDialog() {
        loadingDialog = Dialog(requireContext())
        loadingDialog.setContentView(R.layout.dialog_loading)
        loadingDialog.setCancelable(false)  // Prevent user interaction during loading
    }


    private fun uriToByteArray(uri: Uri): ByteArray {
        return requireContext().contentResolver.openInputStream(uri)?.use { it.readBytes() } ?: ByteArray(0)
    }

    companion object {
        private const val HEADLINE_IMAGE_REQUEST_CODE = 1001
        private const val IMAGE_REQUEST_CODE = 1002
    }
}

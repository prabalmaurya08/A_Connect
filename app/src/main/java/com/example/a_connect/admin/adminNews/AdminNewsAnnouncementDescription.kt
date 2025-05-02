package com.example.a_connect.admin.adminNews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.a_connect.R
import com.example.a_connect.admin.adminJob.AdminJobDetailArgs
import com.example.a_connect.admin.adminNews.mvvm.NewsRepository
import com.example.a_connect.admin.adminNews.mvvm.NewsViewModel
import com.example.a_connect.admin.adminNews.mvvm.NewsViewModelFactory
import com.example.a_connect.databinding.FragmentAdminNewsAnnouncementDescriptionBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.getValue


class AdminNewsAnnouncementDescription : Fragment() {
    private var _binding: FragmentAdminNewsAnnouncementDescriptionBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsId: String
    private val viewModel: NewsViewModel by viewModels {
        NewsViewModelFactory(NewsRepository())
    }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentAdminNewsAnnouncementDescriptionBinding.inflate(inflater, container, false)
        arguments?.let {
            newsId = AdminNewsAnnouncementDescriptionArgs.fromBundle(it).newsId // Retrieve jobId from Safe Args
        }
        observeViewModel()


        viewModel.loadNewsById(newsId)
        viewModel.selectedNews.observe(viewLifecycleOwner) { news ->
            news?.let {
                binding.newsHeading1.text = it.heading
                binding.description.text = it.description
                binding.Date.text = formatDate(it.createdAt)
                binding.time.text = formatTime(it.createdAt)

                Glide.with(requireContext())
                    .load(it.headlinePhotoUrl)
                    .placeholder(R.drawable.college_image)

                    .into(binding.universityImage)

                Glide.with(requireContext())
                    .load(it.photoUrl)
                    .placeholder(R.drawable.college_image)

                    .into(binding.photo)
            }
        }

        return binding.root

    }
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.loadingState.collect { isLoading ->
                        if (isLoading){
                            binding.loadingAnimationView.visibility= View.VISIBLE
                            binding.cl.visibility = View.GONE
                        }else{
                            binding.loadingAnimationView.visibility= View.GONE
                            binding.cl.visibility = View.VISIBLE
                        }

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
    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    private fun formatTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        return when {
            diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)} mins ago"
            diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)} hrs ago"
            else -> "${diff / (24 * 60 * 60 * 1000)} days ago"
        }
    }

}
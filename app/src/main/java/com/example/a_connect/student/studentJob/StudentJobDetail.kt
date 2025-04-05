package com.example.a_connect.student.studentJob

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import androidx.lifecycle.Observer

import com.example.a_connect.admin.adminJob.mvvm.AdminJobViewModel
import com.example.a_connect.alumni.alumniJob.AlumniJobDetailArgs

import com.example.a_connect.databinding.FragmentStudentJobDetailBinding


class StudentJobDetail : Fragment() {
    private lateinit var binding: FragmentStudentJobDetailBinding
    private lateinit var jobId: String
    private val viewModel: AdminJobViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            jobId = AlumniJobDetailArgs.fromBundle(it).jobId
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentStudentJobDetailBinding.inflate(inflater, container, false).apply {
            this.viewModel = this@StudentJobDetail.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        setupObservers()
        viewModel.fetchJobDetails(jobId)


        return binding.root
    }

    private fun setupObservers() {
        viewModel.jobDetails.observe(viewLifecycleOwner, Observer { job ->
            Log.d("AdminJobDetails", "Job details received: ${viewModel.logoUrl}")
            if (job != null) {
                Glide.with(this)
                    .load(job.logo)
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal) // Placeholder while loading
                    .error(android.R.drawable.ic_menu_report_image) // Fallback for errors
                    .into(binding.CompanyIcon)
                Log.d("AdminJobDetails", "Job details received: ${job.logo}") // Log job details when received
                // Populate the UI with job details here
            } else {
                binding.CompanyIcon.setImageResource(android.R.drawable.ic_menu_report_image)
                Log.d("AdminJobDetail", "No job details received") // Log if no job details are received
            }
        })


        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            Log.d("AdminJobDetail", "Loading state: $isLoading") // Log the loading state
            // Show or hide the progress bar based on the loading state
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) {
                Log.e("AdminJobDetail", "Error: $errorMessage") // Log error messages
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

    }

}


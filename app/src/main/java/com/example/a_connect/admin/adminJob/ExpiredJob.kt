package com.example.a_connect.admin.adminJob

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a_connect.R
import com.example.a_connect.admin.adminJob.mvvm.AdminJobAdapter
import com.example.a_connect.admin.adminJob.mvvm.AdminJobViewModel
import com.example.a_connect.databinding.FragmentExpiredJobBinding
import java.util.Date

class ExpiredJob : Fragment() {
    private lateinit var binding: FragmentExpiredJobBinding
    private lateinit var viewModel: AdminJobViewModel
    private lateinit var adapter: AdminJobAdapter
    private var progressDialog: Dialog? = null
    private lateinit var listener: OnJobClickListener

    // Define a listener interface
    interface OnJobClickListener {
        fun onJobClicked(jobId: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnJobClickListener) {
            listener = context // Assign the listener
        } else {
            throw ClassCastException("$context must implement OnAdminJobClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpiredJobBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[AdminJobViewModel::class.java]

        setupRecyclerView()
        setupObservers()
        fetchJobs()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = AdminJobAdapter(
            onJobClick = { jobId ->
                listener.onJobClicked(jobId.jobId)
            },
            onJobDelete = { job ->
                deleteJob(job.jobId) // Trigger delete action
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        // Observe job list from ViewModel
        viewModel.jobList.observe(viewLifecycleOwner) { jobs ->
            val currentDate = Date()
            val expiredJobs = jobs.filter { job ->
                job.endDate?.toDate()?.before(currentDate) == true
            }

            if (expiredJobs.isEmpty()) {
                binding.emptyStateText.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.emptyStateText.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.submitList(expiredJobs)
            }
            dismissLoadingDialog()
        }

        // Observe success state from ViewModel
        viewModel.success.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Job deleted successfully", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe error state from ViewModel
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchJobs() {
      //  showLoadingDialog()
        viewModel.fetchJobs()
    }

    private fun deleteJob(jobId: String) {
        showLoadingDialog()
        viewModel.deleteJob(jobId)
    }

    private fun showLoadingDialog() {
        if (progressDialog == null) {
            progressDialog = Dialog(requireContext())
            progressDialog?.setCancelable(false)
            progressDialog?.setContentView(R.layout.progress_bar_dialogbox)
        }
        progressDialog?.show()
    }

    private fun dismissLoadingDialog() {
        progressDialog?.dismiss()
    }

    override fun onResume() {
        super.onResume()
        fetchJobs()
    }
}

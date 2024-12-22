package com.example.a_connect.admin.adminJob

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a_connect.R
import com.example.a_connect.admin.adminJob.mvvm.AdminJobAdapter
import com.example.a_connect.admin.adminJob.mvvm.AdminJobViewModel
import com.example.a_connect.databinding.FragmentOngoingJobBinding
import java.util.Date


class OngoingJob : Fragment() {
    private lateinit var binding: FragmentOngoingJobBinding
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
            throw ClassCastException("$context must implement OnJobClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentOngoingJobBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(requireActivity())[AdminJobViewModel::class.java]




        adapter = AdminJobAdapter(
            onJobClick = {jobId ->

                listener.onJobClicked(jobId.jobId)
            /* Handle click */ },
            onJobDelete = { /* Handle delete */
                viewModel.deleteJob(jobId = it.jobId) }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
        showLoadingDialog()
        viewModel.startListeningForJobs()


        // Observe job list from ViewModel
        viewModel.jobList.observe(viewLifecycleOwner) { jobs ->
            val currentDate = Date()
            // Filter jobs for upcoming jobs (startDate > current date)
            val ongoingJobs = jobs.filter { job ->
                val isStartDateValid = job.startDate?.toDate()?.before(currentDate) == true || job.startDate == null
                val isEndDateValid = job.endDate?.toDate()?.after(currentDate) == true || job.endDate == null
                isStartDateValid && isEndDateValid
            }
            dismissLoadingDialog()
            Log.d("AdminJobFragment", "Fetched jobs: $jobs")
            if(ongoingJobs.isEmpty()){

                binding.emptyStateText.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
            else{

                binding.emptyStateText.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
            adapter.submitList(ongoingJobs)



        }

        // Fetch jobs if not already fetched
        if (viewModel.jobList.value.isNullOrEmpty()) {
            viewModel.fetchJobs()
        }

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
    // Function to show the custom loading dialog
    private fun showLoadingDialog() {
        if (progressDialog == null) {
            progressDialog = Dialog(requireContext())
            progressDialog?.setCancelable(false)
            progressDialog?.setContentView(R.layout.progress_bar_dialogbox)
            progressDialog?.show()
        }
    }
    override fun onResume() {
        super.onResume()
        // Fetch jobs when the fragment is resumed (visible)
        showLoadingDialog()
        viewModel.fetchJobs()
    }

    // Function to dismiss the custom loading dialog
    private fun dismissLoadingDialog() {
        progressDialog?.dismiss()
    }


}
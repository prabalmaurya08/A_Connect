package com.example.a_connect.alumni.alumniJob


import android.content.Context

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a_connect.alumni.alumniJob.mvvm.AlumniJobAdapter
import com.example.a_connect.alumni.alumniJob.mvvm.AlumniJobViewModel
import com.example.a_connect.databinding.FragmentAlumniJobBinding
import java.util.Date





class AlumniJob : Fragment() {
    private lateinit var binding: FragmentAlumniJobBinding
    private lateinit var adapter: AlumniJobAdapter
    private lateinit var viewmodel: AlumniJobViewModel

    private lateinit var listener: OnAlumniJobClickListener

    // Define a listener interface
    interface OnAlumniJobClickListener {
        fun onAlumniJobClicked(jobId: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnAlumniJobClickListener) {
            listener = context // Assign the listener
        } else {
            throw ClassCastException("$context must implement OnAdminJobClickListener")
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAlumniJobBinding.inflate(layoutInflater)
        viewmodel= ViewModelProvider(this)[AlumniJobViewModel::class.java]



        adapter= AlumniJobAdapter(
            onJobClick = {
                listener.onAlumniJobClicked(it.jobId)/* Handle click */

            },
            onApply = {
//                Log.d("Fragment", "Apply clicked: ${it.jobId}")
//                Log.d("Fragment", "Apply clicked: ${it.applyLink}")
//                val url = "https://www.naukri.com/"
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                val resolveInfoList = requireContext().packageManager.queryIntentActivities(intent, 0)
//
//                if (resolveInfoList.isNotEmpty()) {
//                    startActivity(intent)
//                } else {
//                    Log.e("Fragment", "No app found to handle the URL")
//                    Toast.makeText(requireContext(), "No app found to open the URL", Toast.LENGTH_SHORT).show()
//                }
            }


                )

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
        viewmodel.startListeningForJobs()

        viewmodel.jobList.observe(viewLifecycleOwner) { jobs ->
            val currentDate = Date()
            // Filter jobs for upcoming jobs (startDate > current date)
            val ongoingJobs = jobs.filter { job ->
                val isStartDateValid = job.startDate?.toDate()?.before(currentDate) == true || job.startDate == null
                val isEndDateValid = job.endDate?.toDate()?.after(currentDate) == true || job.endDate == null
                isStartDateValid && isEndDateValid
            }
            adapter.submitList(ongoingJobs)
        }



        // Fetch jobs if not already fetched
        if (viewmodel.jobList.value.isNullOrEmpty()) {
            viewmodel.fetchJobs()
        }


        return binding.root
    }



}
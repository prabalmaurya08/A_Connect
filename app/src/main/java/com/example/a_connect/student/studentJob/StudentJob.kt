package com.example.a_connect.student.studentJob

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a_connect.R
import com.example.a_connect.alumni.alumniJob.mvvm.AlumniJobAdapter
import com.example.a_connect.alumni.alumniJob.mvvm.AlumniJobViewModel
import com.example.a_connect.databinding.FragmentStudentJobBinding
import java.util.Date


class StudentJob : Fragment() {
    private lateinit var binding: FragmentStudentJobBinding
    private lateinit var adapter: AlumniJobAdapter
    private lateinit var viewmodel: AlumniJobViewModel
    private lateinit var adapterClass: StudentJobAdapterClass
    private lateinit var listener: OnStudentJobClickListener

    // Define a listener interface
    interface OnStudentJobClickListener {
        fun onStudentJobClicked(jobId: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnStudentJobClickListener) {
            listener = context // Assign the listener
        } else {
            throw ClassCastException("$context must implement OnAdminJobClickListener")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentStudentJobBinding.inflate(layoutInflater)
        viewmodel= ViewModelProvider(this)[AlumniJobViewModel::class.java]



        adapterClass= StudentJobAdapterClass(
            onJobClick = {
                listener.onStudentJobClicked(it.jobId)/* Handle click */

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

        binding.studentJobRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.studentJobRecyclerview.adapter = adapterClass
        viewmodel.startListeningForJobs()

        viewmodel.jobList.observe(viewLifecycleOwner) { jobs ->
            val currentDate = Date()
            // Filter jobs for upcoming jobs (startDate > current date)
            val ongoingJobs = jobs.filter { job ->
                val isStartDateValid = job.startDate?.toDate()?.before(currentDate) == true || job.startDate == null
                val isEndDateValid = job.endDate?.toDate()?.after(currentDate) == true || job.endDate == null
                isStartDateValid && isEndDateValid
            }
            adapterClass.submitList(ongoingJobs)
        }



        // Fetch jobs if not already fetched
        if (viewmodel.jobList.value.isNullOrEmpty()) {
            viewmodel.fetchJobs()
        }
        return binding.root
    }



}
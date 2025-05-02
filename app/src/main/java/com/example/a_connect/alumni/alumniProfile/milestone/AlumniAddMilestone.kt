package com.example.a_connect.alumni.alumniProfile.milestone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.a_connect.R
import com.example.a_connect.UserSessionManager
import com.example.a_connect.databinding.FragmentAlumniAddMilestoneBinding
import com.example.a_connect.student.studentProfile.milestone.StudentMilestone
import com.example.a_connect.student.studentProfile.milestone.StudentMilestoneViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AlumniAddMilestone : Fragment() {
    private var _binding: FragmentAlumniAddMilestoneBinding? = null
    private val binding get() = _binding!!

    private lateinit var milestoneViewModel: StudentMilestoneViewModel
    private lateinit var studentEmail: String

    private lateinit var etMilestoneTitle: TextInputEditText
    private lateinit var etMilestoneDescription: TextInputEditText
    private lateinit var etMilestoneDate: TextInputEditText
    private lateinit var btnSaveMilestone: MaterialButton
    private lateinit var sessionManager: UserSessionManager


    private lateinit var datePicker: MaterialDatePicker<Long>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAlumniAddMilestoneBinding.inflate(inflater, container, false)
        milestoneViewModel = ViewModelProvider(this)[StudentMilestoneViewModel::class.java]

        // Get current student email from session manager
        sessionManager = UserSessionManager(requireContext())

        studentEmail=sessionManager.getCurrentUserEmail().toString()

        // Initialize the views
        etMilestoneTitle = binding.etMilestoneTitle
        etMilestoneDescription = binding.etMilestoneDescription
        etMilestoneDate = binding.etMilestoneDate
        btnSaveMilestone = binding.btnSaveMilestone

        // Date picker dialog
        val today = Calendar.getInstance().timeInMillis
        datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(today)
            .setTitleText("Select Milestone Date")
            .build()

        // Handle date picker
        etMilestoneDate.setOnClickListener {
            datePicker.show(childFragmentManager, "DATE_PICKER")
        }

        datePicker.addOnPositiveButtonClickListener {
            // Format selected date and set it in the date EditText
            val selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
            etMilestoneDate.setText(selectedDate)
        }

        // Save button click listener
        btnSaveMilestone.setOnClickListener {
            val title = etMilestoneTitle.text.toString().trim()
            val description = etMilestoneDescription.text.toString().trim()
            val date = etMilestoneDate.text.toString().trim()

            if (title.isNotEmpty() && description.isNotEmpty() && date.isNotEmpty()) {
                // Create a StudentMilestone object and save it using ViewModel
                val milestone = StudentMilestone(title = title, description = description, date = date)
                milestoneViewModel.addAlumniMilestone(studentEmail, milestone)

                // Optionally, show a success message
                Toast.makeText(requireContext(), "Milestone Saved", Toast.LENGTH_SHORT).show()
                // Clear fields after saving
                etMilestoneTitle.text = null
                etMilestoneDescription.text = null
                etMilestoneDate.text = null
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe the milestones list to update the UI
        milestoneViewModel.milestones.observe(viewLifecycleOwner, Observer {
            // Update your RecyclerView with the list of milestones
        })




        return binding.root
    }


}
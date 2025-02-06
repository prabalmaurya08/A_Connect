package com.example.a_connect.admin.adminJob

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.a_connect.R
import com.example.a_connect.admin.adminJob.mvvm.AdminJobViewModel
import com.example.a_connect.databinding.FragmentAdminAddJobBinding // Make sure you import the correct binding class

import com.google.android.material.snackbar.Snackbar
import java.util.Calendar


class AdminAddJob : Fragment() {
    private lateinit var binding: FragmentAdminAddJobBinding // Make sure this is the generated class
    private val viewModel: AdminJobViewModel by viewModels()

    // Media picker launcher
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // Call ViewModel's uploadLogo method to handle the URI
                viewModel.uploadLogo(it)
            }
        }


    private val calendar = Calendar.getInstance()

    // DatePickerDialog listener for Start Date
    private val startDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val date = "${dayOfMonth}/${monthOfYear + 1}/${year}"
            viewModel.startDate.value = date
        }

    // DatePickerDialog listener for End Date
    private val endDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val date = "${dayOfMonth}/${monthOfYear + 1}/${year}"
            viewModel.endDate.value = date
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using DataBindingUtil and the generated class
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_add_job, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Setup Data Binding and ViewModel
        binding.addCompanyIcon.setOnClickListener {
            pickImageLauncher.launch("image/*") // Open media picker for image selection
        }

        // Observe changes in logo URL from the ViewModel and update the ImageView
        viewModel.logoUrl.observe(viewLifecycleOwner) { logoUrl ->
            if (logoUrl.isNotEmpty()) {
                // Load the image into ImageView using Glide
                Glide.with(requireContext())
                    .load(logoUrl)
                    .into(binding.logoPreview)
            }
        }

        // Handle the button click for creating a job
        binding.createJobButton.setOnClickListener {
            if (viewModel.companyName.value.isNullOrEmpty() || viewModel.logoUrl.value.isNullOrEmpty()) {
                Snackbar.make(binding.root, "Please provide all required fields", Snackbar.LENGTH_SHORT).show()
            } else {
                viewModel.addJob()
            }
        }

        // Observe success and error messages from ViewModel
        viewModel.success.observe(viewLifecycleOwner) { success ->
            if (success) {
                Snackbar.make(binding.root, "Job created successfully!", Snackbar.LENGTH_SHORT).show()
               // findNavController().navigate(R.id.adminJob)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Snackbar.make(binding.root, "Error: $error", Snackbar.LENGTH_SHORT).show()
            }
        }

        // Open DatePicker for Start Date
        binding.startRegistrationDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                startDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // Open DatePicker for End Date
        binding.endRegistrationDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                endDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        setUpGenderSpinner()
        //setupToolbar()
        handleBackPress()
        return binding.root
    }




    private fun setUpGenderSpinner() {
        // Initialize the Spinner with the gender array from strings.xml
        val genderArray = resources.getStringArray(R.array.gender_array)

        // Create an adapter for the Spinner
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item, genderArray
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the adapter to the Spinner
        binding.gender.adapter = adapter

        // Set an item selected listener on the Spinner
        binding.gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing
            }

            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedView: View?,
                position: Int,
                id: Long
            ) {
                // Update the ViewModel with the selected gender
                val selectedGender = genderArray[position]
                viewModel.gender.value = selectedGender
            }
        }
    }
        private fun setupToolbar() {
            val toolbar = binding.appbar.findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
            toolbar.setNavigationOnClickListener {
                // Navigate back when the back icon is pressed
                findNavController().navigateUp()
            }
        }

        private fun handleBackPress() {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                // Navigate back when the device back button is pressed
                findNavController().navigateUp()
            }
        }

}


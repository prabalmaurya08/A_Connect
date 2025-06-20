package com.example.a_connect.login

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.a_connect.R
import com.example.a_connect.UserSessionManager

import com.example.a_connect.admin.adminCollegeProfile.mvvm.CollegeProfileRepository
import com.example.a_connect.admin.adminCollegeProfile.mvvm.CollegeProfileViewModel
import com.example.a_connect.admin.adminCollegeProfile.mvvm.EditProfileViewModelFactory
import com.example.a_connect.databinding.FragmentAlumniLoginBinding
import com.example.a_connect.login.mvvm.LoginViewModel

class AlumniLogin : Fragment() {

    // Initialize user session manager
    private lateinit var sessionManager: UserSessionManager

    private lateinit var binding: FragmentAlumniLoginBinding
    private lateinit var loadingDialog: Dialog
    private var listener: OnAlumniScreenClicked? = null
    private val repository = CollegeProfileRepository()
    private val viewModel: CollegeProfileViewModel by viewModels {

        EditProfileViewModelFactory(repository)
    }

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var graduationYearsSpinner: Spinner
    private lateinit var collegeNamesSpinner: Spinner

    private lateinit var progressBar: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {

            is OnAlumniScreenClicked -> {
                listener = context
            }
            else -> {
                throw ClassCastException("$context must implement OnAlumniScreenClicked")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAlumniLoginBinding.inflate(layoutInflater)
        initLoadingDialog()

        // Initialize Spinners and ProgressBar
        graduationYearsSpinner = binding.aluminiLoginGraduationYear
        collegeNamesSpinner = binding.aluminiLoginCollegeName
        progressBar = binding.progressBar

        // Get the collegeId and load the profile data
        val collegeId = "collegeId123" // Example collegeId

        viewModel.loadProfileData(collegeId)

        // Observe graduation years and college names LiveData
        viewModel.graduationYears.observe(viewLifecycleOwner) { graduationYears ->
            val graduationAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, graduationYears)
            graduationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            graduationYearsSpinner.adapter = graduationAdapter
        }

        viewModel.collegeNames.observe(viewLifecycleOwner) { collegeNames ->
            val collegeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, collegeNames)
            collegeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            collegeNamesSpinner.adapter = collegeAdapter
        }

        authentication()

        setup()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = UserSessionManager(requireContext())
    }

    private fun authentication() {

        // Handle login button clicks
        binding.submitButton.setOnClickListener {
            loadingDialog.show()

            val email = binding.aluminiLoginEmailAddress.text.toString()
            val graduationYear = graduationYearsSpinner.selectedItem.toString().toInt()
            val collegeName = collegeNamesSpinner.selectedItem.toString()
            Log.d("AlumniLogin", "Email: $email, Graduation Year: $graduationYear, College Name: $collegeName")

            // Show progress bar while authenticating
            progressBar.visibility = View.VISIBLE

            // Call alumniLogin
            loginViewModel.alumniLogin(email, graduationYear, collegeName)
        }

        // Observe alumni login result
        loginViewModel.alumniLoginResult.observe(viewLifecycleOwner, Observer { result ->
            val (success, message) = result
           loadingDialog.dismiss()

            if (success) {
                // Save user credentials to SharedPreferences
                val email = binding.aluminiLoginEmailAddress.text.toString()
                val graduationYear = graduationYearsSpinner.selectedItem.toString().toInt()
                val collegeName = collegeNamesSpinner.selectedItem.toString()

                // Save to session manager
                sessionManager.createAlumniSession(email, graduationYear, collegeName)

                listener?.onAlumniSubmitClicked()
                // Navigate to home screen on successful login
                showToast("Login Successful")
                loadingDialog.dismiss()
            } else {
                // Show error message
                showToast(message ?: "Login Failed")
                loadingDialog.dismiss()
            }
        })
    }
    // Initialize the loading dialog with Lottie animation
    private fun initLoadingDialog() {
        loadingDialog = Dialog(requireContext())
        loadingDialog.setContentView(R.layout.dialog_loading)
        loadingDialog.setCancelable(false)  // Prevent user interaction during loading
    }



    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setup() {

        binding.aluminiLoginAsAdmin.setOnClickListener {
            listener?.onAlumniAdminClicked()
        }
    }

    // Making this interface to use components inside the ViewPager
    interface OnAlumniScreenClicked {
        fun onAlumniSubmitClicked()
        fun onAlumniAdminClicked()
    }
}

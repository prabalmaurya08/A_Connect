package com.example.a_connect.login

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.a_connect.R
import com.example.a_connect.UserSessionManager
import com.example.a_connect.admin.adminCollegeProfile.mvvm.CollegeProfileRepository
import com.example.a_connect.admin.adminCollegeProfile.mvvm.CollegeProfileViewModel
import com.example.a_connect.admin.adminCollegeProfile.mvvm.EditProfileViewModelFactory
import com.example.a_connect.databinding.FragmentStudentLoginBinding
import com.example.a_connect.login.mvvm.LoginViewModel

class StudentLogin : Fragment() {


    private lateinit var sessionManager: UserSessionManager

    private lateinit var binding: FragmentStudentLoginBinding
    private lateinit var loadingDialog: Dialog
    private var listener: OnStudentScreenClicked? = null
    private val repository = CollegeProfileRepository()
    private val viewModel: CollegeProfileViewModel by viewModels {

        EditProfileViewModelFactory(repository)
    }

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var graduationYearsSpinner: Spinner
    private lateinit var collegeNamesSpinner: Spinner

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {

            is OnStudentScreenClicked -> {
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

        // Inflate the layout for this fragment
        binding=FragmentStudentLoginBinding.inflate(layoutInflater)
        initLoadingDialog()

        // Initialize Spinners and ProgressBar
        graduationYearsSpinner = binding.studentLoginGraduationYear
        collegeNamesSpinner = binding.studentLoginCollegeName
        //progressBar = binding.progressBar

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
        binding.studentLoginAsAdmin.setOnClickListener {
            listener?.onStudentAdminClicked()
        }

        authentication()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = UserSessionManager(requireContext())
    }
    // Initialize the loading dialog with Lottie animation
    private fun initLoadingDialog() {
        loadingDialog = Dialog(requireContext())
        loadingDialog.setContentView(R.layout.dialog_loading)
        loadingDialog.setCancelable(false)  // Prevent user interaction during loading
    }



    private fun authentication() {

        // Handle login button clicks
        binding.submitButton.setOnClickListener {

            val email = binding.studentLoginEmailAddress.text.toString()
            val graduationYear = graduationYearsSpinner.selectedItem.toString().toInt()
            val collegeName = collegeNamesSpinner.selectedItem.toString()
            Log.d("StudentLogin", "Email: $email, Graduation Year: $graduationYear, College Name: $collegeName")

            // Show progress bar while authenticating
           // progressBar.visibility = View.VISIBLE
            loadingDialog.show()

            // Call alumniLogin
            loginViewModel.studentLogin(email, graduationYear, collegeName)
        }

        // Observe alumni login result
        loginViewModel.studentLoginResult.observe(viewLifecycleOwner, Observer { result ->
            val (success, message) = result
            // Hide progress bar after operation
         //   progressBar.visibility = View.GONE

            if (success) {

                val email = binding.studentLoginEmailAddress.text.toString()
                val graduationYear = graduationYearsSpinner.selectedItem.toString().toInt()
                val collegeName = collegeNamesSpinner.selectedItem.toString()
                sessionManager.createStudentSession(email, graduationYear, collegeName)

                listener?.onStudentSubmitClicked()
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

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // Making this interface to use components inside the ViewPager
    interface OnStudentScreenClicked {
        fun onStudentSubmitClicked()
        fun onStudentAdminClicked()
    }
}
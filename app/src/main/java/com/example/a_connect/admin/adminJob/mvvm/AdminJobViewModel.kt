package com.example.a_connect.admin.adminJob.mvvm

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.a_connect.admin.adminJob.AdminJob
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class AdminJobViewModel : ViewModel() {
    private val repository = AdminJobRepository()

    // Job fields for two-way binding
    val companyName = MutableLiveData<String>()
    val logoUrl = MutableLiveData<String>() // For image preview and storage
    val endDate = MutableLiveData<String>()
    val cutOff = MutableLiveData<String>()
    val startDate = MutableLiveData<String>()
    val degree = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val eligibility = MutableLiveData<String>()
    val designation = MutableLiveData<String>()
    val graduationYear = MutableLiveData<String>()
    val roleAndResponsibility = MutableLiveData<String>()
    val location = MutableLiveData<String>()
    val websiteLink = MutableLiveData<String>()
    val applyLink = MutableLiveData<String>()
    val gender = MutableLiveData<String>()

    // Success and error handling LiveData
    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> get() = _success

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // LiveData to hold the list of jobs
    private val _jobList = MutableLiveData<List<AdminJobDataClass>>()
    val jobList: LiveData<List<AdminJobDataClass>> get() = _jobList

    // LiveData to hold a single job's details
    private val _jobDetails = MutableLiveData<AdminJobDataClass>()
    val jobDetails: LiveData<AdminJobDataClass> get() = _jobDetails

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    // Function to upload logo to Firestore Storage
    fun uploadLogo(imageUri: Uri) {
        repository.uploadLogo(
            imageUri,
            onSuccess = { url ->
                logoUrl.value = url
            },
            onError = { exception ->
                _error.value = exception.message
            }
        )
    }

    // Function to add a job to Firestore
    fun addJob() {
        val job = AdminJobDataClass(
            companyName = companyName.value.orEmpty(),
            logo = logoUrl.value.orEmpty(),
            endDate = convertToTimestamp(endDate.value),
            startDate = convertToTimestamp(startDate.value),
            description = description.value.orEmpty(),
            eligibility = eligibility.value.orEmpty(),
            designation = designation.value.orEmpty(),
            location = location.value.orEmpty(),
            cutOff = cutOff.value.orEmpty(),
            degree = degree.value.orEmpty(),
            graduationYear = graduationYear.value.orEmpty(),
            applyLink = applyLink.value.orEmpty(),
            websiteLink = websiteLink.value.orEmpty(),
            roleAndRes = roleAndResponsibility.value.orEmpty(),
            gender = gender.value.orEmpty()
        )

        repository.addJob(
            job,
            onSuccess = { _success.value = true },
            onError = { exception -> _error.value = exception.message }
        )
    }

    // Function to convert date string to Firestore Timestamp
    private fun convertToTimestamp(dateString: String?): Timestamp? {
        val dateFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
        return try {
            dateString?.let {
                val date = dateFormat.parse(it)
                Timestamp(date)
            }
        } catch (e: Exception) {
            null // Handle invalid date format gracefully
        }
    }

    // Function to fetch jobs from Firestore
    fun fetchJobs() {
        repository.getJobs(
            onResult = { jobs ->
                Log.d("AdminJobRepository", "Fetched jobs size: ${jobs.size}")
                _jobList.value = jobs // Update the LiveData with the fetched jobs
            },
            onError = { exception ->
                _error.value = exception.message // Update error LiveData with the error message
            }
        )
    }

//    // Helper method to calculate how long ago a job was posted
    fun calculateTimeAgo(startDate: Timestamp?): String {
        startDate?.toDate()?.let { start ->
            val diffInMillis = System.currentTimeMillis() - start.time
            val diffInHours = diffInMillis / (1000 * 60 * 60)
            return if (diffInHours < 24) {
                "$diffInHours hours ago"
            } else {
                val diffInDays = diffInMillis / (1000 * 60 * 60 * 24)
                "$diffInDays days ago"
            }
        }
        return "N/A"
    }

    // Helper method to format Timestamp to date string (e.g., "January 5, 2025")
    fun formatDate(timestamp: Timestamp?): String {
        return timestamp?.toDate()?.let {
            SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(it)
        } ?: "N/A"
    }


    // Start listening for real-time updates
    fun startListeningForJobs() {
        repository.listenForJobs(
            onResult = { jobs ->
                _jobList.value = jobs  // Update LiveData with new job list
            },
            onError = { exception ->
                Log.e("AdminJobViewModel", "Error fetching jobs: ${exception.message}")
            }
        )
    }

    fun fetchJobDetails(jobId: String) {
        _loading.value = true
        Log.d("AdminJobViewModel", "Fetching job details for Job ID: $jobId") // Log the job ID being passed
        repository.getJobById(
            jobId = jobId,
            onResult = { job ->
                Log.d("AdminJobViewModel", "Job details fetched successfully: $job") // Log the fetched job details
                _jobDetails.value = job
                populateJobFields(job)
                _loading.value = false // Hide the loading spinner
            },
            onError = { exception ->
                Log.e("AdminJobViewModel", "Error fetching job details: ${exception.message}") // Log error message
                _error.value = exception.message
                _loading.value = false // Hide the loading spinner
            }
        )
    }

    // Function to populate fields with job details for two-way binding
    private fun populateJobFields(job: AdminJobDataClass) {
        companyName.value = job.companyName
        logoUrl.value = job.logo
        endDate.value = formatDate(job.endDate)
        startDate.value = formatDate(job.startDate)
        description.value = job.description
        eligibility.value = job.eligibility
        designation.value = job.designation
        location.value = job.location
        cutOff.value = job.cutOff
        degree.value = job.degree
        graduationYear.value = job.graduationYear
        applyLink.value = job.applyLink
        websiteLink.value = job.websiteLink
        roleAndResponsibility.value = job.roleAndRes
        gender.value = job.gender
    }

    // Stop listening when no longer needed
    fun stopListeningForJobs() {
        repository.stopListening()
    }

   //  Method to delete a job (example placeholder, add implementation)
    // Method to delete a job
    fun deleteJob(jobId: String) {
        repository.deleteJob(
            jobId,
            onSuccess = {
                _success.value = true // Set success status
            },
            onError = { exception ->
                _error.value = exception.message // Set error message
            }
        )
    }


}

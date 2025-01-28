package com.example.a_connect.alumni.alumniJob.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlumniJobViewModel: ViewModel() {
    private val repository = AlumniJobRepository()


    private val _jobList = MutableLiveData<List<AlumniJobDataClass>>()
    val jobList: LiveData<List<AlumniJobDataClass>> get() = _jobList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error
    fun fetchJobs(){
        repository.getJobs(
            onResult = { jobs ->
                _jobList.value = jobs
            },
            onError = { exception ->
                _error.value = exception.message
            }
        )
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

}
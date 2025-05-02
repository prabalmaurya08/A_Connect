package com.example.a_connect.student.studentProfile.milestone

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class StudentMilestoneViewModel : ViewModel() {

    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val milestoneRepository = StudentMilestoneRepository(firebaseFirestore)

    // LiveData to observe milestones list
    val milestones: MutableLiveData<List<StudentMilestone>> = MutableLiveData()
    // LiveData to observe operation success status
    val isOperationSuccessful: MutableLiveData<Boolean> = MutableLiveData()

    // Add milestone using coroutine
    fun addMilestone(currentUserEmail: String, milestone: StudentMilestone) {
        viewModelScope.launch {
            val isSuccess = milestoneRepository.addMilestone(currentUserEmail, milestone)
            isOperationSuccessful.value = isSuccess
            if (isSuccess) {
                // Refresh milestones list after adding a new milestone
                getMilestones(currentUserEmail)
            }
        }
    }

    // Get milestones using coroutine
    fun getMilestones(currentUserEmail: String) {
        viewModelScope.launch {
            try {
                val milestoneList = milestoneRepository.getMilestones(currentUserEmail)
                Log.d("Milestones", "Fetched milestones: $milestoneList")
                milestones.value = milestoneList
            } catch (e: Exception) {
                Log.e("Milestones", "Error fetching milestones", e)
            }
        }
    }





    // Add milestone using coroutine
    fun addAlumniMilestone(currentUserEmail: String, milestone: StudentMilestone) {
        viewModelScope.launch {
            val isSuccess = milestoneRepository.addAlumniMilestone(currentUserEmail, milestone)
            isOperationSuccessful.value = isSuccess
            if (isSuccess) {
                // Refresh milestones list after adding a new milestone
                getAlumniMilestones(currentUserEmail)
            }
        }
    }

    // Get milestones using coroutine
    fun getAlumniMilestones(currentUserEmail: String) {
        viewModelScope.launch {
            try {
                val milestoneList = milestoneRepository.getAlumniMilestones(currentUserEmail)
                Log.d("Milestones", "Fetched milestones: $milestoneList")
                milestones.value = milestoneList
            } catch (e: Exception) {
                Log.e("Milestones", "Error fetching milestones", e)
            }
        }
    }
}

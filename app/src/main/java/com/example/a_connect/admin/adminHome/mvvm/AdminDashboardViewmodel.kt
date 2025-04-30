package com.example.a_connect.admin.adminHome.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AdminDashboardViewmodel(
    private val repository: AdminDashboardRepository
) : ViewModel() {

    private val _dashboardStats = MutableLiveData<AdminDashboardDataClass>()
    val dashboardStats: LiveData<AdminDashboardDataClass> = _dashboardStats

    fun fetchDashboardStats() {
        viewModelScope.launch {
            try {
                val alumni = repository.getAlumniCount()
                val students = repository.getStudentCount()
                val posts = repository.getPostCount()
                val news = repository.getNewsCount()
                val jobs = repository.getJobCount()

                _dashboardStats.value = AdminDashboardDataClass(
                    alumniCount = alumni,
                    studentCount = students,
                    postCount = posts,
                    newsCount = news,
                    jobCount = jobs
                )
                Log.d("DashboardViewModel", "Alumni: $alumni, Students: $students, Posts: $posts, News: $news, Jobs: $jobs")

            } catch (e: Exception) {
                // Handle errors
                Log.e("DashboardViewModel", "Error fetching stats", e)
            }
        }
    }
}
class AdminDashboardViewModelFactory(
    private val repository: AdminDashboardRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminDashboardViewmodel::class.java)) {
            return AdminDashboardViewmodel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


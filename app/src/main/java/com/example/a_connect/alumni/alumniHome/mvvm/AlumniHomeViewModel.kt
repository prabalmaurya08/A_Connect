package com.example.a_connect.alumni.alumniHome.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a_connect.admin.adminCollegeProfile.mvvm.CollegeProfileRepository

class  AlumniHomeViewModel : ViewModel() {
    private val repository = CollegeProfileRepository()


    // LiveData to hold the image URL for alumni home
    val imageUrl = MutableLiveData<String?>()

    fun fetchImageUrl(collegeId: String) {
        repository.getProfile(collegeId, onSuccessListener = { profile ->
            imageUrl.value = profile.imageUrl // This assumes your repository is returning a profile with imageUrl
        }, onFailureListener = {
            // Handle failure, maybe show an error message
        })
    }
}

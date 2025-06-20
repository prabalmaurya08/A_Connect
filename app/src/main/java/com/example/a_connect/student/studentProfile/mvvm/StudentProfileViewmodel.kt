package com.example.a_connect.student.studentProfile.mvvm

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch

class StudentProfileViewmodel(private val repository: StudentProfileRepository): ViewModel() {
    val isLoading = MutableLiveData<Boolean>()  // 🔹 Added isLoading


    // Profile fields
    val profilePic = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val bio = MutableLiveData<String>()

   val headline = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()

    val gender = MutableLiveData<String>()
    val degreeSpecialization = MutableLiveData<String>()
    val linkedinUrl = MutableLiveData<String>()
    val instagramUrl = MutableLiveData<String>()
    val gmailUrl = MutableLiveData<String>()
    val threadsUrl = MutableLiveData<String>()

    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> get() = _updateSuccess

    // Fetch user profile from Firestore
    fun fetchUserProfile(userEmail: String) {
        isLoading.value = true
        viewModelScope.launch {
            val profile = repository.getUserProfile(userEmail)
            profile?.let {
                profilePic.postValue(it.profilePic)
                name.postValue(it.name)
                bio.postValue(it.bio)
                headline.postValue(it.headline!!)


                phoneNumber.postValue(it.phoneNumber!!)

                gender.postValue(it.gender)
                degreeSpecialization.postValue(it.degreeSpecialization)
                linkedinUrl.postValue(it.linkedinUrl!!)
                gmailUrl.postValue(it.gmailUrl!!)
                threadsUrl.postValue(it.threadsUrl!!)
            }

            isLoading.value = false
        }
    }

    // Update user profile in Firestore (only changed fields)
    fun updateUserProfile(userEmail: String, newProfilePicUri: Uri?) {
        isLoading.value = true
        // Fetch existing profile data first (to preserve other fields)
        viewModelScope.launch {
            val existingProfile = repository.getUserProfile(userEmail)

            // Merge the existing profile data with the new data
            val profileData = mutableMapOf<String, Any?>()

            existingProfile?.let {
                // Upload the new profile picture if it's provided
                val uploadedImageUrl = newProfilePicUri?.let { uri ->
                    repository.uploadProfilePic(userEmail, uri)
                }

                profileData["profilePic"] = uploadedImageUrl ?: it.profilePic
                profileData["name"] = name.value ?: it.name
                profileData["bio"] = bio.value ?: it.bio
                profileData["headline"] = headline.value ?: it.headline


                profileData["phoneNumber"] = phoneNumber.value ?: it.phoneNumber

                profileData["gender"] = gender.value ?: it.gender
                profileData["degreeSpecialization"] = degreeSpecialization.value ?: it.degreeSpecialization
                profileData["linkedinUrl"] = linkedinUrl.value ?: it.linkedinUrl
                profileData["instagramUrl"] = instagramUrl.value ?: it.instagramUrl
                profileData["gmailUrl"] = gmailUrl.value ?: it.gmailUrl
                profileData["threadsUrl"] = threadsUrl.value ?: it.threadsUrl
            }

            // Update the Firestore data with merged data
            val result = repository.updateUserProfile(userEmail, profileData)
            _updateSuccess.postValue(result)
            isLoading.value = false
        }
    }
}

class StudentEditProfileViewModelFactory(private val repository: StudentProfileRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentProfileViewmodel::class.java)) {
            return StudentProfileViewmodel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
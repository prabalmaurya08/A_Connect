package com.example.a_connect.admin.adminCollegeProfile.mvvm

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CollegeProfileViewModel(private val repository: CollegeProfileRepository) : ViewModel() {

    // Profile fields
    val universityName = MutableLiveData<String>()
    val tagline = MutableLiveData<String>()
    val bio = MutableLiveData<String>()
    val collegeEmail = MutableLiveData<String>()
    val linkedinUrl = MutableLiveData<String>()
    val instagramUrl = MutableLiveData<String>()
    val gmailUrl = MutableLiveData<String>()
    val threadsUrl = MutableLiveData<String>()
    val imageUrl = MutableLiveData<String?>()

    val graduationYears = MutableLiveData<List<String>>()
    val collegeNames = MutableLiveData<List<String>>()
    // String representation for data binding
    val graduationYearsString = MutableLiveData<String>()
    val collegeNamesString = MutableLiveData<String>()

    // Profile update state
    private val _isProfileUpdated = MutableLiveData<Boolean>()
    val isProfileUpdated: LiveData<Boolean> get() = _isProfileUpdated

    // Loading state for UI
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> get() = _loadingState

    // New flag to track if the image is loaded
    private val _isImageLoaded = MutableLiveData<Boolean>()
    val isImageLoaded: LiveData<Boolean> get() = _isImageLoaded

    private val _errorState = MutableLiveData<String?>()
    val errorState: LiveData<String?> get() = _errorState

    fun loadProfileData(collegeId: String) {
        _loadingState.value = true
        repository.getProfile(
            collegeId,
            onSuccessListener = {
                universityName.value = it.universityName
                bio.value = it.bio
                tagline.value = it.tagline
                collegeEmail.value = it.collegeEmail
                linkedinUrl.value = it.linkedinUrl
                instagramUrl.value = it.instagramUrl
                gmailUrl.value = it.gmailUrl
                threadsUrl.value = it.threadsUrl
                imageUrl.value = it.imageUrl
//                graduationYears.value = it.graduationYears ?: emptyList()
//                collegeNames.value = it.collegeNames ?: emptyList()

                fetchGraduationYearsAndCollegeNames(collegeId)
                _loadingState.value = false

                // Mark the image as loaded when profile data is fetched successfully
                _isImageLoaded.value = true
            },
            onFailureListener = {
                _loadingState.value = false
                _errorState.value = it.message // Or a user-frien
                // Handle failure (e.g., show an error message in the UI)
            }
        )
    }

    fun updateProfile(collegeId: String) {
        _loadingState.value = true
        val profileData = CollegeProfileDataClass(
            universityName = universityName.value ?: "",
            bio = bio.value ?: "",
            tagline = tagline.value ?: "",
            collegeEmail = collegeEmail.value ?: "",
            linkedinUrl = linkedinUrl.value ?: "",
            instagramUrl = instagramUrl.value ?: "",
            gmailUrl = gmailUrl.value ?: "",
            threadsUrl = threadsUrl.value ?: "",
            imageUrl = imageUrl.value ?: "",
            graduationYears = graduationYears.value ?: emptyList(),
            collegeNames = collegeNames.value ?: emptyList()
        )
        repository.updateProfileData(
            collegeId, profileData,
            onSuccessListener = {
                _isProfileUpdated.value = true
                _loadingState.value = false
            },
            onFailureListener = {
                _loadingState.value = false
                // Handle failure (e.g., show an error message in the UI)
            }
        )
    }

    // Upload image to Firebase Storage and save URL to Firestore
    fun uploadImage(collegeId: String, imageUri: Uri) {
        _loadingState.value = true
        repository.uploadImage(
            collegeId, imageUri,
            onSuccessListener = { uploadedImageUrl ->
                imageUrl.value = uploadedImageUrl // Update the image URL in ViewModel
                _loadingState.value = false
                _isImageLoaded.value = true
            },
            onFailureListener = {
                _loadingState.value = false
            }
        )
    }

    fun deleteImage(collegeId: String) {
        _loadingState.value = true
        repository.deleteImage(
            collegeId,
            onSuccessListener = {
                imageUrl.value = null // Clear image URL after deletion
                _loadingState.value = false
            },
            onFailureListener = {
                _loadingState.value = false
                // Handle failure (e.g., show an error message in the UI)
            }
        )
    }
    // Save Graduation Years and College Names
    fun saveGraduationYearsAndCollegeNames(collegeId: String) {
        _loadingState.value = true

        // Convert String values to List<String>
        graduationYears.value = graduationYearsString.value?.split(",")?.map { it.trim() }
        collegeNames.value = collegeNamesString.value?.split(",")?.map { it.trim() }

        val graduationYearsList = graduationYears.value ?: emptyList()
        val collegeNamesList = collegeNames.value ?: emptyList()

        repository.saveGraduationYearsAndCollegeNames(
            collegeId,
            graduationYearsList,
            collegeNamesList,
            callback = { success ->
                _loadingState.value = false
                if (success) {
                    // Successfully saved
                } else {
                    _errorState.value = "Failed to save graduation years and college names"
                }
            }
        )
    }

    // Fetch Graduation Years and College Names
    private fun fetchGraduationYearsAndCollegeNames(collegeId: String) {
        _loadingState.value = true
        repository.fetchGraduationYearsAndCollegeNames(
            collegeId,
            callback = { graduationYearsList, collegeNamesList ->
                graduationYears.value = graduationYearsList
                collegeNames.value = collegeNamesList

                // Convert List<String> back to String representation for binding
                graduationYearsString.value = graduationYearsList.joinToString(", ")
                collegeNamesString.value = collegeNamesList.joinToString(", ")

                _loadingState.value = false
            }
        )
    }
}

class EditProfileViewModelFactory(
    private val repository: CollegeProfileRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollegeProfileViewModel::class.java)) {
            return CollegeProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



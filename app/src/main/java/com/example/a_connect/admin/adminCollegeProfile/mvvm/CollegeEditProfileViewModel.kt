package com.example.a_connect.admin.adminCollegeProfile.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CollegeEditProfileViewModel(private val repository: CollegeProfileRepository) :ViewModel() {

    val universityName = MutableLiveData<String>()
    val bio = MutableLiveData<String>()
    val collegeEmail = MutableLiveData<String>()
    val linkedinUrl = MutableLiveData<String>()
    val instagramUrl = MutableLiveData<String>()
    val gmailUrl = MutableLiveData<String>()
    val threadsUrl = MutableLiveData<String>()

    private val _isProfileUpdated = MutableLiveData<Boolean>()
    val isProfileUpdated: LiveData<Boolean> get() = _isProfileUpdated

    fun loadProfileData(collegeId: String) {
        repository.getProfile(
            collegeId,
            onSuccessListener = {
                universityName.value = it.universityName
                bio.value = it.bio
                collegeEmail.value = it.collegeEmail
                linkedinUrl.value = it.linkedinUrl
                instagramUrl.value = it.instagramUrl
                gmailUrl.value = it.gmailUrl
                threadsUrl.value = it.threadsUrl

            }, onFailureListener = {

            }

        )
    }

    fun updateProfile(collegeId:String){
        val profileData=CollegeProfileDataClass(
            universityName = universityName.value ?: "",
            bio = bio.value ?: "",
            collegeEmail = collegeEmail.value ?: "",
            linkedinUrl = linkedinUrl.value ?: "",
            instagramUrl = instagramUrl.value ?: "",
            gmailUrl = gmailUrl.value ?: "",
            threadsUrl = threadsUrl.value ?: ""
        )
        repository.updateProfileData(collegeId,profileData,
            onSuccessListener = {
                _isProfileUpdated.value=true
            },
            onFailureListener = {

            }
        )

    }

}


class EditProfileViewModelFactory(
    private val repository: CollegeProfileRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollegeEditProfileViewModel::class.java)) {
            return CollegeEditProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

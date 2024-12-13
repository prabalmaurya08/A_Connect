package com.example.a_connect.admin.adminCollegeProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdminCollegeProfileViewModel : ViewModel() {

    // LiveData for the data fields
    private val _universityName = MutableLiveData<String>()
    val universityName: LiveData<String> get() = _universityName

    private val _bio = MutableLiveData<String>()
    val bio: LiveData<String> get() = _bio

    private val _collegeEmail = MutableLiveData<String>()
    val collegeEmail: LiveData<String> get() = _collegeEmail

    private val _linkedinLink = MutableLiveData<String>()
    val linkedinLink: LiveData<String> get() = _linkedinLink

    private val _instagramLink = MutableLiveData<String>()
    val instagramLink: LiveData<String> get() = _instagramLink

    private val _gmailLink = MutableLiveData<String>()
    val gmailLink: LiveData<String> get() = _gmailLink

    private val _threadsLink = MutableLiveData<String>()
    val threadsLink: LiveData<String> get() = _threadsLink

    // Functions to update data
    fun updateUniversityName(name: String) {
        _universityName.value = name
    }

    fun updateBio(bio: String) {
        _bio.value = bio
    }

    fun updateCollegeEmail(email: String) {
        _collegeEmail.value = email
    }

    fun updateLinkedinLink(link: String) {
        _linkedinLink.value = link
    }

    fun updateInstagramLink(link: String) {
        _instagramLink.value = link
    }

    fun updateGmailLink(link: String) {
        _gmailLink.value = link
    }

    fun updateThreadsLink(link: String) {
        _threadsLink.value = link
    }
}

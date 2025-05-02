package com.example.a_connect.alumni.alumniProfile.mvvm2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AlumniProfileDetailViewModel(
    private val repository: AlumniProfileDetailRepository
) : ViewModel() {


    private val _profileData = MutableLiveData<AlumniProfileDetailDataClass?>()
    val profileData: LiveData<AlumniProfileDetailDataClass?> get() = _profileData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadAlumniProfile(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val data = repository.getUserProfile(email)
            _profileData.value = data
            _isLoading.value = false
        }
    }
}


class AlumniProfileDetailViewModelFactory(
    private val repository: AlumniProfileDetailRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlumniProfileDetailViewModel::class.java)) {
            return AlumniProfileDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

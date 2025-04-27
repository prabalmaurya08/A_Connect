package com.example.a_connect.alumni.alumniExplore



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.a_connect.alumni.alumniProfile.mvvm.AlumniProfileDataClass

import kotlinx.coroutines.launch

class LocationViewModel(private val locationRepository: LocationRepository) : ViewModel() {


    fun updateUserLocation(userEmail: String, latitude: Double, longitude: Double, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isUpdated = locationRepository.updateUserLocation(userEmail, latitude, longitude)
            onResult(isUpdated) // Callback to notify UI about the success/failure
        }
    }


//    fun getUserLocation(userEmail: String, onResult: (Map<String, Double>?) -> Unit) {
//        viewModelScope.launch {
//            val location = locationRepository.getUserLocation(userEmail)
//            onResult(location) // Callback to notify UI with the location data
//        }
//    }
//    private val _alumniLocations = MutableLiveData<List<Triple<String, String, Pair<Double, Double>>>>()
//    val alumniLocations: LiveData<List<Triple<String, String, Pair<Double, Double>>>> get() = _alumniLocations
//
//
//    fun fetchAlumniLocations() {
//        viewModelScope.launch {
//            val locations = locationRepository.getAllAlumniLocations()
//            _alumniLocations.postValue(locations)
//        }
//    }

    private val _alumniLocations = MutableLiveData<List<AlumniProfileDataClass>>()
    val alumniLocations: LiveData<List<AlumniProfileDataClass>> get() = _alumniLocations
    // Fetch all alumni locations for displaying them on a map
    fun fetchAllAlumniLocations() {
        viewModelScope.launch {
            _alumniLocations.value = locationRepository.getAllAlumniLocations()
        }
    }
}



class LocationViewModelFactory(private val repository: LocationRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            return LocationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


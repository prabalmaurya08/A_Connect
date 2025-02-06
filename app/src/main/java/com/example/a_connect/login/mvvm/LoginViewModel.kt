package com.example.a_connect.login.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel:ViewModel() {
    private val repository = LoginRepository()

    private val _alumniLoginResult = MutableLiveData<Pair<Boolean, String?>>()
    val alumniLoginResult: LiveData<Pair<Boolean, String?>> = _alumniLoginResult


    private val _studentLoginResult = MutableLiveData<Pair<Boolean, String?>>()
    val studentLoginResult: LiveData<Pair<Boolean, String?>> = _alumniLoginResult

    fun alumniLogin(email: String, graduationYear: Int, collegeName: String) {
        repository.alumniLogin(email, graduationYear, collegeName) { success, message ->
            _alumniLoginResult.postValue(Pair(success, message))
        }
    }
    fun studentLogin(email: String, graduationYear: Int, collegeName: String) {
        repository.studentLogin(email, graduationYear, collegeName) { success, message ->
            _studentLoginResult.postValue(Pair(success, message))
        }
    }
}
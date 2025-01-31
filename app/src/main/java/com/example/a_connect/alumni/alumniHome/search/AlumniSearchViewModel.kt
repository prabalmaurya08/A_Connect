package com.example.a_connect.alumni.alumniHome.search

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class AlumniSearchViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    private val _alumniList = MutableLiveData<List<AlumniSearchDataClass>>()
    val alumniList: LiveData<List<AlumniSearchDataClass>> get() = _alumniList

    // Search alumni based on query
    fun searchAlumni(query: String) {
        if (query.isEmpty()) {
            // If query is empty, clear the list (show no results)
            _alumniList.value = emptyList()
            return
        }

        // Perform search with Firestore
        firestore.collection("Alumni")
            .orderBy("name")
            .startAt(query.lowercase()).endAt(query.lowercase() + "\uf8ff")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                // Process the snapshot and update the list
                val alumniList = snapshot?.documents?.mapNotNull {
                    it.toObject(AlumniSearchDataClass::class.java)
                } ?: emptyList()

                // Update the LiveData
                _alumniList.value = alumniList
            }
    }

    // Clear the alumni list (for empty search query)
    fun clearAlumniList() {
        _alumniList.value = emptyList()
    }
}

package com.example.a_connect.alumni.alumniJob.mvvm

import android.util.Log
import com.example.a_connect.admin.adminJob.mvvm.AdminJobDataClass

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage

class AlumniJobRepository {
    private val firestore = FirebaseFirestore.getInstance()

    private val collection = firestore.collection("Jobs")

    // Get all jobs from Firestore
    fun getJobs(onResult: (List<AlumniJobDataClass>) -> Unit, onError: (Exception) -> Unit) {
       collection
            .get()
            .addOnSuccessListener { snapshot ->
                val jobs = snapshot.toObjects(AlumniJobDataClass::class.java)
                Log.d("Firestore", "Fetched jobs: $jobs")
                onResult(jobs)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }


    private var listenerRegistration: ListenerRegistration? = null
    // Real-time listener for job data
    fun listenForJobs(onResult: (List<AlumniJobDataClass>) -> Unit, onError: (Exception) -> Unit) {
        listenerRegistration = collection.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                onError(exception)
                return@addSnapshotListener
            }

            snapshot?.let {
                val jobs = it.toObjects(AlumniJobDataClass::class.java)
                onResult(jobs)
            }
        }
    }
}
package com.example.a_connect.admin.adminJob.mvvm

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class AdminJobRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // Add a new job to Firestore
    fun addJob(job: AdminJobDataClass, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val jobId = firestore.collection("Jobs").document().id
        val jobWithId = job.copy(jobId = jobId)
        firestore.collection("Jobs").document(jobId)
            .set(jobWithId)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onError(exception) }
    }

    fun uploadLogo(imageUri: Uri, onSuccess: (String) -> Unit, onError: (Exception) -> Unit) {
        // Generate a unique file name using UUID or timestamp (to avoid duplicates or invalid characters)
        val fileName = "logo_${System.currentTimeMillis()}.jpg"  // Or use a UUID.randomUUID().toString()

        // Create a reference to Firebase Storage
        val logoRef = storage.reference.child("logos/$fileName")

        // Upload the image to Firebase Storage
        logoRef.putFile(imageUri)
            .addOnSuccessListener {
                logoRef.downloadUrl.addOnSuccessListener { uri ->
                    onSuccess(uri.toString()) // Return the download URL
                }
            }
            .addOnFailureListener { exception ->
                onError(exception) // Handle failure
            }
    }



    // Get all jobs from Firestore
    fun getJobs(onResult: (List<AdminJobDataClass>) -> Unit, onError: (Exception) -> Unit) {
        firestore.collection("Jobs")
            .get()
            .addOnSuccessListener { snapshot ->
                val jobs = snapshot.toObjects(AdminJobDataClass::class.java)
                Log.d("Firestore", "Fetched jobs: $jobs")
                onResult(jobs)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    private val jobsCollection = firestore.collection("Jobs")

    private var listenerRegistration: ListenerRegistration? = null

    // Real-time listener for job data
    fun listenForJobs(onResult: (List<AdminJobDataClass>) -> Unit, onError: (Exception) -> Unit) {
        listenerRegistration = jobsCollection.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                onError(exception)
                return@addSnapshotListener
            }

            snapshot?.let {
                val jobs = it.toObjects(AdminJobDataClass::class.java)
                onResult(jobs)
            }
        }
    }

    // Stop listening for changes
    fun stopListening() {
        listenerRegistration?.remove()
    }
    fun getJobById(jobId: String, onResult: (AdminJobDataClass) -> Unit, onError: (Exception) -> Unit) {
        Log.d("AdminRepo", "Fetching job with ID: $jobId") // Log the job ID being used
        firestore.collection("Jobs").document(jobId).get()
            .addOnSuccessListener { document ->
                Log.d("AdminRepo", "Document fetched: ${document.id}") // Log document ID
                if (document.exists()) {
                    document.toObject(AdminJobDataClass::class.java)?.let { job ->
                        Log.d("AdminRepo", "Job details: $job") // Log job details
                        onResult(job)
                    } ?: run {
                        Log.d("AdminRepo", "Job not found in Firestore.") // Log if job is not found
                        onError(Exception("Job not found"))
                    }
                } else {
                    Log.d("AdminRepo", "Document does not exist.") // Log if document does not exist
                    onError(Exception("Job not found"))
                }
            }
            .addOnFailureListener { exception ->
                Log.e("AdminRepo", "Error fetching job details: ${exception.message}") // Log error message
                onError(exception)
            }
    }
    // Method to delete job from Firestore
    fun deleteJob(jobId: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        firestore.collection("Jobs")
            .document(jobId)
            .delete()
            .addOnSuccessListener {
                Log.d("AdminRepo", "Job successfully deleted.")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("AdminRepo", "Error deleting job: ${exception.message}")
                onError(exception)
            }
    }


}
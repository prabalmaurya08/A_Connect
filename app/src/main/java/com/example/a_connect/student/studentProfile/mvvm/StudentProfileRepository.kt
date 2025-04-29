package com.example.a_connect.student.studentProfile.mvvm

import android.net.Uri
import com.example.a_connect.alumni.alumniProfile.mvvm.AlumniProfileDataClass
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class StudentProfileRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val studentCollection = firestore.collection("Students")
    private val storage = FirebaseStorage.getInstance()

    // Fetch user profile from Firestore
    suspend fun getUserProfile(userEmail: String): StudentProfileDataClass? {
        return try {
            val document = studentCollection.document(userEmail).get().await()
            document.toObject(StudentProfileDataClass::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // Update user profile in Firestore (only changed fields)
    suspend fun updateUserProfile(userEmail: String, profile: Map<String, Any?>): Boolean {
        return try {
            studentCollection.document(userEmail).update(profile).await()
            true
        } catch (e: Exception) {
            false
        }
    }


    // Upload profile picture to Firebase Storage and return the download URL
    suspend fun uploadProfilePic(userEmail: String, imageUri: Uri): String? {
        val storageRef = storage.reference.child("profile_pics/$userEmail.jpg")
        return try {
            val uploadTask = storageRef.putFile(imageUri).await()
            uploadTask.metadata?.reference?.downloadUrl?.await().toString()
        } catch (e: Exception) {
            null
        }
    }

}
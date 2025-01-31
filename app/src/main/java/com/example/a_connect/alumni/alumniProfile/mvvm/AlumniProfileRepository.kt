package com.example.a_connect.alumni.alumniProfile.mvvm

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class AlumniProfileRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val alumniCollection = firestore.collection("Alumni")
    private val storage = FirebaseStorage.getInstance()

    // Fetch user profile from Firestore
    suspend fun getUserProfile(userEmail: String): AlumniProfileDataClass? {
        return try {
            val document = alumniCollection.document(userEmail).get().await()
            document.toObject(AlumniProfileDataClass::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // Update user profile in Firestore (only changed fields)
    suspend fun updateUserProfile(userEmail: String, profile: Map<String, Any?>): Boolean {
        return try {
            alumniCollection.document(userEmail).update(profile).await()
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

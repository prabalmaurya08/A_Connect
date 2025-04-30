package com.example.a_connect.alumni.alumniProfile.mvvm2


import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AlumniProfileDetailRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val alumniCollection = firestore.collection("Alumni")

    // Fetch user profile from Firestore
    suspend fun getUserProfile(userEmail: String): AlumniProfileDetailDataClass? {
        return try {
            // Fetch the document using the user's email
            val document = alumniCollection.document(userEmail).get().await()

            // Log the document retrieval
            if (document.exists()) {
                Log.d("AlumniProfileDetailRepository", "Document found for email: $userEmail")
                document.toObject(AlumniProfileDetailDataClass::class.java)
            } else {
                Log.e("AlumniProfileDetailRepository", "No document found for email: $userEmail")
                null
            }
        } catch (e: Exception) {
            Log.e("AlumniProfileDetailRepository", "Error fetching profile data: ", e)
            null
        }
    }
}

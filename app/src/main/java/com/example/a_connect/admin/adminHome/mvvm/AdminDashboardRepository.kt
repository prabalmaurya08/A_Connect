package com.example.a_connect.admin.adminHome.mvvm

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AdminDashboardRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun getAlumniCount(): Int {
        return firestore.collection("Alumni").get().await().size()
    }

    suspend fun getStudentCount(): Int {
        return firestore.collection("Students").get().await().size()
    }

    suspend fun getPostCount(): Int {
        return firestore.collection("Post").get().await().size()
    }

    suspend fun getNewsCount(): Int {
        return firestore.collection("news").get().await().size()
    }

    suspend fun getJobCount(): Int {
        return firestore.collection("Jobs").get().await().size()
    }
    suspend fun getAllAlumni(): List<AlumniItem> {
        val snapshot = firestore.collection("Alumni").get().await()

        return snapshot.documents.mapNotNull { doc ->
            val college = doc.getString("collegeName") ?: return@mapNotNull null
            val email = doc.id // stored as document ID
            AlumniItem(email = email, college = college)
        }
    }
    suspend fun getAllStudent(): List<StudentItem> {
        val snapshot = firestore.collection("Students").get().await()

        return snapshot.documents.mapNotNull { doc ->
            val college = doc.getString("collegeName") ?: return@mapNotNull null
            val email = doc.id // stored as document ID
            StudentItem(email = email, college = college)
        }
    }

}

package com.example.a_connect.student.studentProfile.milestone

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class StudentMilestoneRepository(private val firebaseFirestore: FirebaseFirestore) {

    // Suspend function to add a milestone
    suspend fun addMilestone(currentUserEmail: String, milestone: StudentMilestone): Boolean {
        return try {
            val milestoneCollection = firebaseFirestore.collection("Students")
                .document(currentUserEmail)  // Using the email as the document ID
                .collection("milestones")

            milestoneCollection.add(milestone).await() // Using Kotlin's await to suspend the function until Firestore operation completes
            true
        } catch (e: Exception) {
            false
        }
    }

    // Suspend function to get milestones
    suspend fun getMilestones(currentUserEmail: String): List<StudentMilestone> {
        return try {
            val milestoneCollection = firebaseFirestore.collection("Students")
                .document(currentUserEmail)
                .collection("milestones")

            val result = milestoneCollection.get().await()
            result.toObjects(StudentMilestone::class.java) // Convert Firestore documents to Milestone objects
        } catch (e: Exception) {
            emptyList() // Return an empty list in case of an error
        }
    }




    //for alumni

    suspend fun addAlumniMilestone(currentUserEmail: String, milestone: StudentMilestone): Boolean {
        return try {
            val milestoneCollection = firebaseFirestore.collection("Alumni")
                .document(currentUserEmail)  // Using the email as the document ID
                .collection("milestones")

            milestoneCollection.add(milestone).await() // Using Kotlin's await to suspend the function until Firestore operation completes
            true
        } catch (e: Exception) {
            false
        }
    }

    // Suspend function to get milestones
    suspend fun getAlumniMilestones(currentUserEmail: String): List<StudentMilestone> {
        return try {
            val milestoneCollection = firebaseFirestore.collection("Alumni")
                .document(currentUserEmail)
                .collection("milestones")

            val result = milestoneCollection.get().await()
            result.toObjects(StudentMilestone::class.java) // Convert Firestore documents to Milestone objects
        } catch (e: Exception) {
            emptyList() // Return an empty list in case of an error
        }
    }
}

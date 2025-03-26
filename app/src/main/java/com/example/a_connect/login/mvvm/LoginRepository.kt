package com.example.a_connect.login.mvvm


import com.example.a_connect.SharedPreferencesHelper
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale


class LoginRepository {
    private val db = FirebaseFirestore.getInstance()

    fun alumniLogin(email: String, graduationYear: Int, collegeName: String, callback: (Boolean, String?) -> Unit) {
        if (email.isEmpty()) {
            callback(false, "Enter a Valid Email Address.")
            return
        }
        else if (graduationYear.toString().isEmpty()) {
            callback(false, "Enter a Valid Graduation Year.")
            return
        }
        else if (collegeName.isEmpty()) {
            callback(false, "Enter a Valid College Name.")
            return
        }

        val alumniRef = db.collection("Alumni").document(email)

        alumniRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val dbGraduationYear = document.getLong("graduationYear")?.toInt()
                    val dbCollegeName =
                        document.getString("collegeName")?.lowercase(Locale.getDefault()) // Convert stored college name to lowercase

                    if (dbGraduationYear == graduationYear && dbCollegeName == collegeName.lowercase(
                            Locale.getDefault()
                        )) {

                        // Convert input to lowercase

                        // Save current user's email to SharedPreferences after successful login
                        SharedPreferencesHelper.saveCurrentUserEmail(email)
                        callback(true, null) // Authentication successful
                    } else {
                        callback(false, "Graduation Year or College Name does not match.")
                    }
                } else {
                    callback(false, "User not found.")
                }
            }
            .addOnFailureListener { exception ->
                callback(false, exception.message)
            }
    }

    fun studentLogin(email: String, graduationYear: Int, collegeName: String, callback: (Boolean, String?) -> Unit) {
        // Check if any of the inputs are empty
        if (email.isEmpty()) {
            callback(false, "Enter a Valid Email Address.")
            return
        } else if (graduationYear.toString().isEmpty()) {
            callback(false, "Enter a Valid Graduation Year.")
            return
        } else if (collegeName.isEmpty()) {
            callback(false, "Enter a Valid College Name.")
            return
        }

        val studentRef = db.collection("Users").document("Student")
            .collection(email)
            .document(email)

        studentRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val dbGraduationYear = document.getLong("graduationYear")?.toInt()
                    val dbCollegeName =
                        document.getString("collegeName")?.lowercase(Locale.ROOT) // Convert stored college name to lowercase

                    if (dbGraduationYear == graduationYear && dbCollegeName == collegeName.lowercase(
                            Locale.ROOT
                        )
                    ) { // Convert input to lowercase
                        callback(true, null) // Authentication successful
                    } else {
                        callback(false, "Graduation Year or College Name does not match.")
                    }
                } else {
                    callback(false, "User not found.")
                }
            }
            .addOnFailureListener { exception ->
                callback(false, exception.message)
            }
    }

    // Logout function (clear SharedPreferences data)
    fun logout() {
        SharedPreferencesHelper.clearUserData()
    }

}
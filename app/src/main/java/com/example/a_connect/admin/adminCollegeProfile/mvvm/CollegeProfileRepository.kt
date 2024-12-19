package com.example.a_connect.admin.adminCollegeProfile.mvvm

import com.google.firebase.firestore.FirebaseFirestore

class CollegeProfileRepository {
    private val db=FirebaseFirestore.getInstance()
    private val collection=db.collection("collegeProfile")


    fun getProfile(
        collegeId:String,
        onSuccessListener:(CollegeProfileDataClass)->Unit,
        onFailureListener:(Exception)->Unit

    ){
        collection.document(collegeId).get().addOnSuccessListener {
            val profile=it.toObject(CollegeProfileDataClass::class.java)
            if (profile != null) {
                onSuccessListener(profile)
            }
            else{
                onFailureListener(Exception("Profile not found"))
            }

        }.addOnFailureListener {
            onFailureListener(it)

        }



    }

    fun updateProfileData(
        collegeId: String,
        profileData: CollegeProfileDataClass,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit

    ){
        collection.document(collegeId).set(profileData).addOnSuccessListener {
            onSuccessListener()
        }.addOnFailureListener {
            onFailureListener(it)
        }

    }
}
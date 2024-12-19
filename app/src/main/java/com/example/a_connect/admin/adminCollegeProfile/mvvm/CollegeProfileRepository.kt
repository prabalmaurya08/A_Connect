package com.example.a_connect.admin.adminCollegeProfile.mvvm

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CollegeProfileRepository {
    private val db=FirebaseFirestore.getInstance()
    private val collection=db.collection("collegeProfile")

    private val storage = FirebaseStorage.getInstance()
    private val storageRef: StorageReference = storage.reference



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



    // Upload image to Firebase Storage and return the image URL
    fun uploadImage(
        collegeId: String,
        imageUri: Uri,
        onSuccessListener: (String) -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        val imageRef = storageRef.child("college_images/$collegeId.jpg")
        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Save the image URL in Firestore
                    saveImageUrlToFirestore(collegeId, uri.toString(), onSuccessListener, onFailureListener)
                }
            }
            .addOnFailureListener { exception ->
                onFailureListener(exception)
            }
    }

    // Save image URL to Firestore
    private fun saveImageUrlToFirestore(collegeId: String, imageUrl: String, onSuccessListener: (String) -> Unit, onFailureListener: (Exception) -> Unit) {
        val profileRef = db.collection("collegeProfile").document(collegeId)

        profileRef.update("imageUrl", imageUrl)
            .addOnSuccessListener {
                onSuccessListener(imageUrl)
            }
            .addOnFailureListener {
                //onFailureListener()
            }
    }

    // Delete image from Firebase Storage
    fun deleteImage(
        collegeId: String,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        val imageRef = storageRef.child("college_images/$collegeId.jpg")
        imageRef.delete()
            .addOnSuccessListener {
                // Successfully deleted image
                onSuccessListener()
            }
            .addOnFailureListener { exception ->
                onFailureListener(exception)
            }
    }
}
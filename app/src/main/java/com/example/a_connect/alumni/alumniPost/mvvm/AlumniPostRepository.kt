package com.example.a_connect.alumni.alumniPost.mvvm

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.a_connect.SharedPreferencesHelper
import com.example.a_connect.alumni.alumniJob.mvvm.AlumniJobDataClass
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class AlumniPostRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun createPost(post: AlumniPostDataClass, callback: (Boolean, String?) -> Unit) {
        val currentUserEmail = SharedPreferencesHelper.getCurrentUserEmail()

        // Ensure the user is logged in
        if (currentUserEmail == null) {
            callback(false, "No user is logged in")
            return
        }

        val postId = firestore.collection("Post").document().id
        val postWithId = post.copy(createdBy = currentUserEmail)

        // Save post to Firestore
        firestore.collection("Post").document(postId).set(postWithId)
            .addOnSuccessListener {
                // Update the user's alumni document
                val alumniRef = firestore.collection("Alumni").document(currentUserEmail)
                alumniRef.update("posts", FieldValue.arrayUnion(postId))
                    .addOnSuccessListener {
                        callback(true, null) // Success
                    }
                    .addOnFailureListener { exception ->
                        callback(false, exception.message) // Error updating user data
                    }
            }
            .addOnFailureListener { exception ->
                callback(false, exception.message) // Error saving post
            }
    }

    private val storage = FirebaseStorage.getInstance()

    fun uploadImageToStorage(imageUri: Uri, callback: (Boolean, String?) -> Unit) {
        val storageRef = storage.reference.child("images/${UUID.randomUUID()}.jpg")

        storageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Get the download URL
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    callback(true, uri.toString()) // Success, return the image URL
                }.addOnFailureListener { exception ->
                    callback(false, exception.message) // Error
                }
            }
            .addOnFailureListener { exception ->
                callback(false, exception.message) // Error
            }
    }






                                                //Method 2 to fetch post

                                            // Fetch post IDs from Alumni collection based on user email
                                            fun getUserPostIds(userEmail: String, callback: (List<String>) -> Unit) {
                                                firestore.collection("Alumni").document(userEmail)
                                                    .get()
                                                    .addOnSuccessListener { document ->
                                                        if (document.exists()) {
                                                            val postIds = document.get("posts") as? List<String> ?: emptyList()
                                                            Log.d("AlumniRepository", "Post IDs for $userEmail: $postIds")
                                                            callback(postIds)
                                                        } else {
                                                            Log.d("AlumniRepository", "No document found for user: $userEmail")
                                                            callback(emptyList())
                                                        }
                                                    }
                                                    .addOnFailureListener { e ->
                                                        Log.e("AlumniRepository", "Error fetching post IDs", e)
                                                        callback(emptyList())
                                                    }
                                            }

                                            // Fetch posts using post IDs from Post collection
                                            fun getUserPostsById(postIds: List<String>, callback: (List<AlumniPostDataClass>) -> Unit) {
                                                if (postIds.isEmpty()) {
                                                    Log.d("AlumniRepository", "No post IDs found, returning empty list")
                                                    callback(emptyList())
                                                    return
                                                }

                                                firestore.collection("Post")
                                                    .whereIn(FieldPath.documentId(), postIds) // Query posts by postId
                                                    .orderBy("createdAt", Query.Direction.DESCENDING)
                                                    .get()
                                                    .addOnSuccessListener { querySnapshot ->
                                                        val posts = querySnapshot.documents.mapNotNull { it.toObject(AlumniPostDataClass::class.java) }
                                                        Log.d("AlumniRepository", "Fetched ${posts.size} posts")
                                                        callback(posts)
                                                    }
                                                    .addOnFailureListener { e ->
                                                        Log.e("AlumniRepository", "Error fetching posts", e)
                                                        callback(emptyList())
                                                    }
                                            }





    fun likePost(postId: String, userEmail: String): Task<Void> {
        val postRef = firestore.collection("Post").document(postId)
        return firestore.runTransaction { transaction ->
            val snapshot = transaction.get(postRef)
            val likes = snapshot.get("likes") as? MutableList<String> ?: mutableListOf()
            if (likes.contains(userEmail)) {
                likes.remove(userEmail)
            } else {
                likes.add(userEmail)
            }
            transaction.update(postRef, "likes", likes)
            null
        }
    }

    fun addComment(postId: String, comment: Comment): Task<Void> {
        val postRef = firestore.collection("Post").document(postId)
        return postRef.update("comments", FieldValue.arrayUnion(comment))
    }
}
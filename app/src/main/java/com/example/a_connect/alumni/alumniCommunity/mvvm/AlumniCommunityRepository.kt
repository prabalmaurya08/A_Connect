package com.example.a_connect.alumni.alumniCommunity.mvvm

import AlumniPostPagingSource
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.a_connect.alumni.alumniPost.mvvm.AlumniPostDataClass
import com.example.a_connect.alumni.alumniPost.mvvm.Comment
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AlumniCommunityRepository {

    private val db = Firebase.firestore
    private val postCollection = db.collection("Post")

    fun getPagedPosts(): Flow<PagingData<AlumniPostDataClass>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { AlumniPostPagingSource(postCollection) }
        ).flow
    }

    // Like or Unlike a post
    suspend fun likePost(postId: String, userId: String) {
        val postRef = postCollection.document(postId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(postRef)
            val likes = snapshot.get("likes") as? MutableList<String> ?: mutableListOf()

            if (likes.contains(userId)) {
                likes.remove(userId) // Unlike
            } else {
                likes.add(userId) // Like
            }
            transaction.update(postRef, "likes", likes)
        }.await()
    }

    // Add a comment to a post
    suspend fun addComment(postId: String, comment: Comment) {
        val postRef = postCollection.document(postId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(postRef)
            val comments = snapshot.get("comments") as? MutableList<Map<String, Any>> ?: mutableListOf()

            comments.add(comment.toMap()) // Convert Comment object to Map
            transaction.update(postRef, "comments", comments)
        }.await()
    }
}

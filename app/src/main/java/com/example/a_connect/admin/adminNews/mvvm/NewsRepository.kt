package com.example.a_connect.admin.adminNews.mvvm

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class NewsRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val newsCollection = firestore.collection("news")
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    suspend fun addNews(news: NewsDataClass, headingPhotoByte: ByteArray?, photoByte: ByteArray?) {
        try {
            // Step 1: Generate a unique ID for the news
            val newsId = newsCollection.document().id
            Log.d("NewsRepository", "Generated News ID: $newsId")

            var headlinePhotoUrl: String? = null
            var photoUrl: String? = null

            // Step 2: Upload the headline image if available
            headingPhotoByte?.takeIf { it.isNotEmpty() }?.let {
                val headingPhotoRef = storageRef.child("news_headline_photo/$newsId.jpg")
                headingPhotoRef.putBytes(it).await()
                headlinePhotoUrl = headingPhotoRef.downloadUrl.await().toString()
                Log.d("NewsRepository", "Headline Image Uploaded: $headlinePhotoUrl")
            }

            // Step 3: Upload the regular photo if available
            photoByte?.takeIf { it.isNotEmpty() }?.let {
                val photoRef = storageRef.child("news_photo/$newsId.jpg")
                photoRef.putBytes(it).await()
                photoUrl = photoRef.downloadUrl.await().toString()
                Log.d("NewsRepository", "Photo Uploaded: $photoUrl")
            }

            // Step 4: Create news object with URLs
            val newsWithUrls = news.copy(
                newsId = newsId,
                headlinePhotoUrl = headlinePhotoUrl ?: "",
                photoUrl = photoUrl ?: ""
            )
            Log.d("NewsRepository", "Saving News to Firestore: $newsWithUrls")

            // Step 5: Save the news to Firestore
            newsCollection.document(newsId).set(newsWithUrls).await()
            Log.d("NewsRepository", "News Saved Successfully: $newsId")

        } catch (e: Exception) {
            Log.e("NewsRepository", "Error in addNews", e)
            throw e
        }
    }

    // Retrieve News List
    suspend fun getAllNews(): List<NewsDataClass> {
        return try {
            val snapshot = newsCollection.get().await()
            snapshot.documents.mapNotNull { it.toObject(NewsDataClass::class.java) }
        } catch (e: Exception) {
            Log.e("NewsRepository", "Error in getAllNews", e)
            throw e
        }
    }

    // Delete News
    suspend fun deleteNews(newsId: String) {
        try {
            // Delete from Firestore
            newsCollection.document(newsId).delete().await()
            Log.d("NewsRepository", "News Deleted from Firestore: $newsId")

            // Delete images from Firebase Storage
            storageRef.child("news_headline_photo/$newsId.jpg").delete().await()
            storageRef.child("news_photo/$newsId.jpg").delete().await()
            Log.d("NewsRepository", "Associated Images Deleted: $newsId")

        } catch (e: Exception) {
            Log.e("NewsRepository", "Error in deleteNews", e)
            throw e
        }
    }
}

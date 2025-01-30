package com.example.a_connect.alumni.alumniPost.mvvm

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AlumniPostViewmodel(application: Application) : AndroidViewModel(application) {

    private val postRepository =AlumniPostRepository()
    private val _postStatus = MutableLiveData<Boolean>()
    val postStatus: LiveData<Boolean> get() = _postStatus

    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> get() = _imageUri

    private val _isUploading = MutableLiveData<Boolean>()
    val isUploading: LiveData<Boolean> get() = _isUploading


    private val _userPost=MutableLiveData<List<AlumniPostDataClass>>()
    val userPost:LiveData<List<AlumniPostDataClass>> get() = _userPost

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    fun createPost(description: String) {
        val imageUri = _imageUri.value
        if (imageUri == null) {
            _postStatus.value = false
            return
        }

        _isUploading.value = true // Disable button while uploading

        // First, upload the image
        postRepository.uploadImageToStorage(imageUri) { success, imageUrl ->
            if (success) {
                // After successful upload, create post
                val post = AlumniPostDataClass(description = description, media = listOf(imageUrl!!))
                postRepository.createPost(post) { postSuccess, _ ->
                    _postStatus.value = postSuccess
                    _isUploading.value = false // Enable button again
                }
            } else {
                _postStatus.value = false
                _isUploading.value = false // Enable button again
            }
        }
    }

//    fun loadUserPost(userEmail:String){
//
//        postRepository.getUserPosts(userEmail){
//            _userPost.value=it
//        }
//
//
//    }


    // Function to load user posts by their email
    fun loadUserPosts(userEmail: String) {
        postRepository.getUserPostIds(userEmail) { postIds ->
            postRepository.getUserPostsById(postIds) { posts ->
                _userPost.value = posts
            }
        }
    }
}
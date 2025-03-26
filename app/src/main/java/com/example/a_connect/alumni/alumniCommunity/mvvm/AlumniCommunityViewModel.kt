package com.example.a_connect.alumni.alumniCommunity.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.a_connect.alumni.alumniPost.mvvm.AlumniPostDataClass
import com.example.a_connect.alumni.alumniPost.mvvm.Comment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AlumniCommunityViewModel(private val repository: AlumniCommunityRepository) : ViewModel() {

    // ✅ Flow for paginated posts
    val pagedPosts: Flow<PagingData<AlumniPostDataClass>> =
        repository.getPagedPosts().cachedIn(viewModelScope)

    // ✅ Like/unlike post
    fun likePost(postId: String, userId: String) {
        viewModelScope.launch {
            repository.likePost(postId, userId)
        }
    }

    // ✅ Add a comment
    fun addComment(postId: String, comment: Comment) {
        viewModelScope.launch {
            repository.addComment(postId, comment)
        }
    }
}
class AlumniCommunityViewModelFactory(private val repository: AlumniCommunityRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlumniCommunityViewModel::class.java)) {
            return AlumniCommunityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

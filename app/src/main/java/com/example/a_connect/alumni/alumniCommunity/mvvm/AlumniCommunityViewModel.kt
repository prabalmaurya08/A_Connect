package com.example.a_connect.alumni.alumniCommunity.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.a_connect.alumni.alumniPost.mvvm.AlumniPostDataClass
import com.example.a_connect.alumni.alumniPost.mvvm.Comment
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AlumniCommunityViewModel(private val repository: AlumniCommunityRepository) : ViewModel() {

    // ✅ Fetch paginated posts
    private val _pagedPosts = repository.getPagedPosts().cachedIn(viewModelScope)

    // ✅ Mutable StateFlow to track real-time post updates
    private val _updatedPosts = MutableStateFlow<Map<String, AlumniPostDataClass>>(emptyMap())
    val updatedPosts: StateFlow<Map<String, AlumniPostDataClass>> = _updatedPosts

    // ✅ Merge paging data with updated post states
    val pagedPosts: Flow<PagingData<AlumniPostDataClass>> = _pagedPosts
        .combine(_updatedPosts) { pagingData, updatedMap ->
            pagingData.map { post -> updatedMap[post.postId] ?: post }
        }
        .distinctUntilChanged()

    // ✅ Like/unlike post and update UI in real-time
    fun likePost(postId: String, userId: String) {
        viewModelScope.launch {
            repository.likePost(postId, userId)

            // ✅ Update UI instantly
            _updatedPosts.update { currentPosts ->
                val updatedPosts = currentPosts.toMutableMap()
                val updatedPost = updatedPosts[postId]?.copy() ?: return@update currentPosts

                if (updatedPost.likes.contains(userId)) {
                    updatedPost.likes -= userId // Unlike
                } else {
                    updatedPost.likes += userId // Like
                }
                updatedPosts[postId] = updatedPost
                updatedPosts
            }
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

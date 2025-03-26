package com.example.a_connect.admin.adminNews.mvvm




import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _newsList = MutableStateFlow<List<NewsDataClass>>(emptyList())
    val newsList: StateFlow<List<NewsDataClass>> get() = _newsList

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> get() = _loadingState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> get() = _errorState

    fun saveNews(news: NewsDataClass, headlinePhotoBytes: ByteArray?, photoBytes: ByteArray?) {
        viewModelScope.launch {
            _loadingState.value = true
            try {
                repository.addNews(news, headlinePhotoBytes, photoBytes)
                loadNews() // Refresh news list after saving
            } catch (e: Exception) {
                _errorState.value = e.message
            } finally {
                _loadingState.value = false
            }
        }
    }

     fun loadNews() {
        viewModelScope.launch {
            _loadingState.value = true
            try {
                _newsList.value = repository.getAllNews()
            } catch (e: Exception) {
                _errorState.value = e.message
            } finally {
                _loadingState.value = false
            }
        }
    }

    fun deleteNews(newsId: String) {
        viewModelScope.launch {
            _loadingState.value = true
            try {
                repository.deleteNews(newsId)
                loadNews() // Refresh news list after deletion
            } catch (e: Exception) {
                _errorState.value = e.message
            } finally {
                _loadingState.value = false
            }
        }
    }

    fun clearError() {
        _errorState.value = null
    }
}

class NewsViewModelFactory(
    private val repository: NewsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
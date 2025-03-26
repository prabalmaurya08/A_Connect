package com.example.a_connect.admin.adminNews.mvvm

data class NewsDataClass(
    val newsId: String = "",
    val heading: String = "",
    val description: String = "",
    val headlinePhotoUrl: String = "",
    val photoUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()


)

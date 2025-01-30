package com.example.a_connect.alumni.alumniPost.mvvm

data class AlumniPostDataClass(
    val postId: String = "",
    val createdBy: String = "",
    val description: String = "",
    val media: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val likes: List<String> = emptyList(),
    val comments: List<Comment> = emptyList()
)
//data class AlumniJobDataClass (
//
//    //  val comments: List<Comment> = emptyList()
//)
data class Comment(
    val userId: String = "",
    val comment: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
package com.example.a_connect.alumni.alumniPost.mvvm

data class AlumniPostDataClass(
    val postId: String = "",
    val createdBy: String = "",
    val name: String = "",
    val description: String = "",
    val media: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    var likes: List<String> = emptyList(),
    val comments: List<Comment> = emptyList()
)

data class Comment(
    val userId: String,
    val text: String,
    val createdAt: Long
) {
    fun toMap(): Map<String, Any> = mapOf(
        "userId" to userId,
        "text" to text,
        "createdAt" to createdAt
    )
}
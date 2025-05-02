package com.example.a_connect.admin.adminHome.mvvm

data class AdminDashboardDataClass(
    val alumniCount: Int = 0,
    val studentCount: Int = 0,
    val postCount: Int = 0,
    val newsCount: Int = 0,
    val jobCount: Int = 0
)

data class AlumniItem(
    val email: String = "",
    val college: String = "",

)

data class StudentItem(
    val email: String = "",
    val college: String = "",

    )

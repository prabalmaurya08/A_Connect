package com.example.a_connect.admin.adminJob.mvvm

import com.google.firebase.Timestamp

data class AdminJobDataClass // Model class for the Job
    (
    val jobId: String = "",
    val companyName: String = "",
    val logo: String = "",

    val startDate: Timestamp?=null ,
    val endDate: Timestamp?=null,
    val description: String = "",
    val degree: String = "",
    val cutOff: String = "",
    val roleAndRes: String = "",
    val graduationYear: String = "",
    val eligibility: String = "",
    val designation: String = "",
    val location: String = "",
    val websiteLink:String = "",
    val applyLink:String = "",
    val gender: String = "any"
)
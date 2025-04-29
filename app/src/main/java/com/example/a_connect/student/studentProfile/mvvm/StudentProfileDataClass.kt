package com.example.a_connect.student.studentProfile.mvvm

data class StudentProfileDataClass (
    val profilePic: String = "",
    val name:String="",
    val bio:String="",
    val headline:String="",


    val phoneNumber:String="",

    val gender:String="",
    val degreeSpecialization:String="",
    val linkedinUrl: String? = "",  // Nullable if the user doesn't have LinkedIn
    val instagramUrl: String? = "",  // Nullable if the user doesn't have Instagram
    val gmailUrl: String? = "",  // Nullable if the user doesn't have Gmail
    val threadsUrl: String? = ""  // Nullable if the user doesn't have Threads

)
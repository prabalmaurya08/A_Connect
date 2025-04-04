package com.example.a_connect.alumni.alumniProfile.mvvm

data class AlumniProfileDataClass (
    val profilePic: String = "",
    val name: String = "",
    val bio: String = "",
    val headline: String = "",
    val industryName: String = "",
    val websiteLink: String? = "",  // Nullable if the user may not have a website
    val phoneNumber: String? = "",  // Nullable if the user doesn't want to provide it

    val location: Map<String, Double>? = null,  // Using Map to store latitude and longitude as separate fields
    val gender: String = "",
    val degreeSpecialization: String = "",

    val linkedinUrl: String? = "",  // Nullable if the user doesn't have LinkedIn
    val instagramUrl: String? = "",  // Nullable if the user doesn't have Instagram
    val gmailUrl: String? = "",  // Nullable if the user doesn't have Gmail
    val threadsUrl: String? = ""  // Nullable if the user doesn't have Threads
)

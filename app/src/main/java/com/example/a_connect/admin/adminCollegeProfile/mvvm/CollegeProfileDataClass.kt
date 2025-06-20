package com.example.a_connect.admin.adminCollegeProfile.mvvm

data class CollegeProfileDataClass(

        var universityName: String = "",
        var tagline: String = "",
        var bio: String = "",
        var collegeEmail: String = "",

        var graduationYears: List<String>? = null,
        var collegeNames: List<String>? = null,
        var linkedinUrl: String = "",
        var instagramUrl: String = "",
        var gmailUrl: String = "",
        var threadsUrl: String = "",


        var imageUrl:String =""



)
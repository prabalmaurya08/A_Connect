package com.example.a_connect

import android.content.Context
import android.content.SharedPreferences

class UserSessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        private const val PREF_NAME = "UserSessionPref"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USER_TYPE = "userType"
        private const val KEY_EMAIL = "email"
        private const val KEY_GRADUATION_YEAR = "graduationYear"
        private const val KEY_COLLEGE_NAME = "collegeName"
        private const val KEY_PASSWORD = "password"

        // User types
        const val USER_TYPE_ALUMNI = "alumni"
        const val USER_TYPE_STUDENT = "student"
        const val USER_TYPE_ADMIN = "admin"
    }

    // Save Alumni session
    fun createAlumniSession(email: String, graduationYear: Int, collegeName: String) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USER_TYPE, USER_TYPE_ALUMNI)
        editor.putString(KEY_EMAIL, email)
        editor.putInt(KEY_GRADUATION_YEAR, graduationYear)
        editor.putString(KEY_COLLEGE_NAME, collegeName)
        editor.apply()
    }

    // Save Student session
    fun createStudentSession(email: String, graduationYear: Int, collegeName: String) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USER_TYPE, USER_TYPE_STUDENT)
        editor.putString(KEY_EMAIL, email)
        editor.putInt(KEY_GRADUATION_YEAR, graduationYear)
        editor.putString(KEY_COLLEGE_NAME, collegeName)
        editor.apply()
    }

    // Save Admin session
    fun createAdminSession(email: String, password: String) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USER_TYPE, USER_TYPE_ADMIN)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_PASSWORD, password)
        editor.apply()
    }

    // Check if user is logged in
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Get user type
    fun getUserType(): String? {
        return sharedPreferences.getString(KEY_USER_TYPE, null)
    }

    // Get current user's email
    fun getCurrentUserEmail(): String? {
        return sharedPreferences.getString(KEY_EMAIL, null)
    }


    // Get alumni details
    fun getAlumniDetails(): Map<String, Any?> {
        val user = HashMap<String, Any?>()
        user["email"] = sharedPreferences.getString(KEY_EMAIL, null)
        user["graduationYear"] = sharedPreferences.getInt(KEY_GRADUATION_YEAR, 0)
        user["collegeName"] = sharedPreferences.getString(KEY_COLLEGE_NAME, null)
        return user
    }

    // Get student details
    fun getStudentDetails(): Map<String, Any?> {
        val user = HashMap<String, Any?>()
        user["email"] = sharedPreferences.getString(KEY_EMAIL, null)
        user["graduationYear"] = sharedPreferences.getInt(KEY_GRADUATION_YEAR, 0)
        user["collegeName"] = sharedPreferences.getString(KEY_COLLEGE_NAME, null)
        return user
    }

    // Get admin details
    fun getAdminDetails(): Map<String, Any?> {
        val user = HashMap<String, Any?>()
        user["email"] = sharedPreferences.getString(KEY_EMAIL, null)
        user["password"] = sharedPreferences.getString(KEY_PASSWORD, null)
        return user
    }

    // Clear session (logout)
    fun logout() {
        editor.clear()
        editor.apply()
    }
}
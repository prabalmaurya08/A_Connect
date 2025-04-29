package com.example.a_connect

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SharedPreferencesHelper {

    private const val PREF_NAME = "AlumniPrefs"
    private lateinit var sharedPreferences: SharedPreferences

    // Initialize SharedPreferences with Application context
    fun init(context: Context) {
        if (!::sharedPreferences.isInitialized) {
            sharedPreferences = context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }
    }

    // Save current user's email
    fun saveCurrentUserEmail(email: String) {
        sharedPreferences.edit() {
            putString("current_user_email", email)
        }
    }
    fun saveStudentName(name:String){
        sharedPreferences.edit() {
            putString("current_student_name", name)
        }
    }
    fun getStudentName(): String? {
        return sharedPreferences.getString("current_student_name", null)
    }
    fun saveCurrentUserName(name: String) {
        sharedPreferences.edit() {
            putString("current_user_name", name)
        }
    }

    // Retrieve current user's email
    fun getCurrentUserEmail(): String? {
        return sharedPreferences.getString("current_user_email", null)
    }
    fun getCurrentUserName(): String? {
        return sharedPreferences.getString("current_user_name", null)
    }

    // Clear user data (logout)
    fun clearUserData() {
        val editor = sharedPreferences.edit()
        editor.remove("current_user_email")
        editor.apply()
    }
}


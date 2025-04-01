package com.example.a_connect

import android.content.Context
import android.content.SharedPreferences

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
        val editor = sharedPreferences.edit()
        editor.putString("current_user_email", email)
        editor.apply()
    }
    fun saveCurrentUserName(name: String) {
        val editor = sharedPreferences.edit()
        editor.putString("current_user_name", name)
        editor.apply()
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


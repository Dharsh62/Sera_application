package com.example.kotlinbasicsapp

import android.content.Context
import android.content.SharedPreferences

class LocalUserManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("sera_user", Context.MODE_PRIVATE)

    // Save login state + phone
    fun setLoggedIn(phone: String) {
        prefs.edit()
            .putBoolean("isLoggedIn", true)
            .putString("phone", phone)
            .apply()
    }

    // Check if logged in
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("isLoggedIn", false)
    }

    // Get logged in phone
    fun getLoggedInPhone(): String? {
        return prefs.getString("phone", null)
    }

    // Logout user
    fun logout() {
        prefs.edit().clear().apply()
    }
}
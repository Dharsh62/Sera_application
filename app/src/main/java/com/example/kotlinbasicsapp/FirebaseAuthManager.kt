package com.example.kotlinbasicsapp.auth

import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthManager {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun logout() {
        auth.signOut()
    }
}
package com.example.kotlinbasicsapp

data class User(
    val name: String = "",
    val phone: String = "",
    val interests: String = "",
    val organization: String = "",
    val role: String = "user",
    val approved: Boolean = false,
    val profileImageUrl: String = ""   // 🔥 NEW
)
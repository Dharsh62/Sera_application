package com.example.kotlinbasicsapp

data class ChatItem(
    val userId: String = "",
    val userName: String = "",
    val lastMessage: String = "",
    val timestamp: Long = 0L
)

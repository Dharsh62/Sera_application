package com.example.kotlinbasicsapp

data class Request(
    val from: String = "",
    val to: String = "",
    val status: String = "pending"
)

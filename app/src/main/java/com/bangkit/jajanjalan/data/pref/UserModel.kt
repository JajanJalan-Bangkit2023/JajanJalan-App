package com.bangkit.jajanjalan.data.pref

data class UserModel(
    val userId : String,
    val email: String,
    val name: String,
    val image: String,
    val password: String,
    val token: String
)

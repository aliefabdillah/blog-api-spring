package com.zuraa.blog_api_spring.model

data class UserRegisterRequest(
    val name: String,
    val email: String,
    val password: String,
)

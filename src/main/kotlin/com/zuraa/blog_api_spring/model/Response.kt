package com.zuraa.blog_api_spring.model

data class ApiSuccessResponse<T>(
    val data: T? = null,
    val status: String,
    val code: Int
)

data class ApiErrorResponse (
    val message: String,
    val status: String,
    val code: Int
)

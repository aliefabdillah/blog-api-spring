package com.zuraa.blog_api_spring.model

import org.springframework.http.HttpStatus

data class ApiSuccessResponse<T>(
    val data: T? = null,
    val status: HttpStatus,
    val code: Int
)

data class ApiErrorResponse (
    val message: String,
    val status: HttpStatus,
    val code: Int
)

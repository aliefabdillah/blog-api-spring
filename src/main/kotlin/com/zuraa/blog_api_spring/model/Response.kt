package com.zuraa.blog_api_spring.model

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.http.HttpStatus
import java.util.*

data class ApiSuccessResponse<T>(
    val data: T? = null,
    val status: HttpStatus,
    val code: Int
)

data class ApiErrorResponse(
    val message: String,
    val status: HttpStatus,
    val code: Int
)

data class UserPublicResponse(
    val id: String,
    val name: String,
    val email: String,
    val createdAt: Date,
    val updatedAt: Date,
)

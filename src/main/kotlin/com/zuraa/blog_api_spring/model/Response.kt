package com.zuraa.blog_api_spring.model

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus
import java.util.*

data class ApiSuccessResponse<T>(
    val status: HttpStatus,
    val code: Int,
    val data: T? = null,
    @JsonInclude(JsonInclude.Include.NON_NULL)  //not showing in body when null
    val pagination: Pagination? = null,
)

data class ApiErrorResponse(
    val status: HttpStatus,
    val code: Int,
    val message: String,
)

data class UserPublicResponse(
    val id: String,
    val name: String,
    val email: String,
    val createdAt: Date,
    val updatedAt: Date,
)

data class ArticleWithAuthor(
    val id: String,
    val title: String,
    val content: String,
    val authorId: String,
    val createdAt: Date,
    val updatedAt: Date,
    val author: UserPublicResponse,
)

data class Pagination(
    val current: Int,
    val perPage: Int,
    val totalPage: Int,
)

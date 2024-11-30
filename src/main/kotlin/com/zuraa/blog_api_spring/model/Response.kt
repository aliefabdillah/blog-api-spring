package com.zuraa.blog_api_spring.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.zuraa.blog_api_spring.entity.Article
import org.springframework.http.HttpStatus
import java.util.*

data class ApiSuccessResponse<T>(
    val status: HttpStatus,
    val code: Int,
    @JsonInclude(JsonInclude.Include.NON_NULL)  //not showing in body when null
    val pagination: Pagination? = null,
    val data: T? = null,
)

data class ApiErrorResponse(
    val status: HttpStatus,
    val code: Int,
    val message: String,
)

data class UserAuthPublicResponse(
    val id: String,
    val name: String,
    val email: String,
    val imgProfile: String,
    val createdAt: Date,
    val updatedAt: Date,
)

data class ArticleWithAuthor(
    val id: String,
    val title: String,
    val content: String,
    val imgProfile: String,
    val authorId: String,
    val createdAt: Date,
    val updatedAt: Date,
    val author: UserAuthPublicResponse,
)

data class UserWithArticle(
    val id: String,
    val name: String,
    val email: String,
    val createdAt: Date,
    val updatedAt: Date,
    val articles: List<Article>
)

data class Pagination(
    val current: Int,
    val perPage: Int,
    val lastPage: Int,
    val total: Int,
)

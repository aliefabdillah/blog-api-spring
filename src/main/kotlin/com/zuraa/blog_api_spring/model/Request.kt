package com.zuraa.blog_api_spring.model

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserRegisterRequest(
    @field:NotBlank
    val name: String,

    @field:NotBlank
    @field:Email
    val email: String,

    @field:Size(min = 6, message = "Password at least must be 8 char long")
    val password: String,
)

data class UserLoginRequest(
    @field:NotBlank
    @field:Email
    val email: String,

    @field:Size(min = 6, message = "Password at least must be 8 char long")
    val password: String,
)

data class CreateArticleRequest(
    @field:NotBlank
    val title: String,

    @field:NotBlank
    val content: String,
)

data class UpdateArticleRequest(
    val title: String?,

    val content: String?,
)

data class ListArticleQuery(
    @field:Min(value = 1, message = "Size page must more than 0")
    val size: Int,

    @field:Min(value = 0, message = "Current page must more than 0")
    val page: Int,

    val title: String,

    val authorName: String,
)

data class UpdateProfileRequest(
    val name: String?,
)
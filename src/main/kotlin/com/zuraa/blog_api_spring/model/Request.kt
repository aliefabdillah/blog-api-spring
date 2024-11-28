package com.zuraa.blog_api_spring.model

import jakarta.validation.constraints.Email
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

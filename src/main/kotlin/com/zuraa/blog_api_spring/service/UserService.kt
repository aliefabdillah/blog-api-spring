package com.zuraa.blog_api_spring.service

import com.zuraa.blog_api_spring.model.*
import org.springframework.security.core.Authentication

interface UserService {
    fun create(request: UserRegisterRequest): ApiSuccessResponse<UserAuthPublicResponse>
    fun auth(request: UserLoginRequest): ApiSuccessResponse<Any>
    fun getUserAuth(auth: Authentication): ApiSuccessResponse<UserAuthPublicResponse>
    fun getUserWithArticle(auth: Authentication): ApiSuccessResponse<UserWithArticle>
}
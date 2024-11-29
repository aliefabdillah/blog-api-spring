package com.zuraa.blog_api_spring.service

import com.zuraa.blog_api_spring.entity.User
import com.zuraa.blog_api_spring.model.ApiSuccessResponse
import com.zuraa.blog_api_spring.model.UserLoginRequest
import com.zuraa.blog_api_spring.model.UserPublicResponse
import com.zuraa.blog_api_spring.model.UserRegisterRequest
import org.springframework.security.core.Authentication

interface UserService {
    fun create(request: UserRegisterRequest): ApiSuccessResponse<UserPublicResponse>
    fun auth(request: UserLoginRequest): ApiSuccessResponse<Any>
    fun getUserAuth(auth: Authentication): ApiSuccessResponse<UserPublicResponse>
}
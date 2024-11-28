package com.zuraa.blog_api_spring.service

import com.zuraa.blog_api_spring.entity.User
import com.zuraa.blog_api_spring.model.ApiSuccessResponse
import com.zuraa.blog_api_spring.model.UserLoginRequest
import com.zuraa.blog_api_spring.model.UserRegisterRequest

interface UserService {
    fun create(request: UserRegisterRequest): ApiSuccessResponse<User>
    fun auth(request: UserLoginRequest): ApiSuccessResponse<Any>
}
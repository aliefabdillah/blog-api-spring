package com.zuraa.blog_api_spring.service.impl

import com.zuraa.blog_api_spring.entity.User
import com.zuraa.blog_api_spring.model.ApiSuccessResponse
import com.zuraa.blog_api_spring.model.UserRegisterRequest
import com.zuraa.blog_api_spring.repository.UserRepository
import com.zuraa.blog_api_spring.service.UserService

class UserServiceImpl(val userRepository: UserRepository) : UserService {
    override fun create(request: UserRegisterRequest): ApiSuccessResponse<User> {
        TODO("Not yet implemented")
    }
}
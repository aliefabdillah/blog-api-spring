package com.zuraa.blog_api_spring.service

import com.zuraa.blog_api_spring.model.*
import org.springframework.security.core.Authentication
import org.springframework.web.multipart.MultipartFile

interface UserService {
    fun create(request: UserRegisterRequest): ApiSuccessResponse<UserAuthPublicResponse>
    fun auth(request: UserLoginRequest): ApiSuccessResponse<Any>
    fun getUserAuth(auth: Authentication): ApiSuccessResponse<UserAuthPublicResponse>
    fun getUserWithArticle(auth: Authentication): ApiSuccessResponse<UserWithArticle>
    fun update(
        auth: Authentication,
        request: UpdateProfileRequest,
        files: MultipartFile
    ): ApiSuccessResponse<UserAuthPublicResponse>
    fun checkEmail(email: String): ApiSuccessResponse<Any>
}
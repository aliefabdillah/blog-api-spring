package com.zuraa.blog_api_spring.controller

import com.zuraa.blog_api_spring.entity.User
import com.zuraa.blog_api_spring.model.ApiSuccessResponse
import com.zuraa.blog_api_spring.model.UserLoginRequest
import com.zuraa.blog_api_spring.model.UserPublicResponse
import com.zuraa.blog_api_spring.model.UserRegisterRequest
import com.zuraa.blog_api_spring.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(val userService: UserService) {

    @PostMapping(value = ["/register"], produces = ["application/json"], consumes = ["application/json"])
    fun registerUser(@RequestBody body: UserRegisterRequest): ApiSuccessResponse<UserPublicResponse> {
        return userService.create(request = body)
    }

    @PostMapping(value = ["/login"], produces = ["application/json"], consumes = ["application/json"])
    fun loginUser(@RequestBody body: UserLoginRequest): ApiSuccessResponse<Any> {
        return userService.auth(body)
    }
}
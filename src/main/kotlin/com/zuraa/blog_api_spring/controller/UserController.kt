package com.zuraa.blog_api_spring.controller

import com.zuraa.blog_api_spring.model.*
import com.zuraa.blog_api_spring.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/user")
class UserController(val userService: UserService) {

    @PostMapping(value = ["/register"], produces = ["application/json"], consumes = ["application/json"])
    fun registerUser(@RequestBody body: UserRegisterRequest): ApiSuccessResponse<UserAuthPublicResponse> {
        return userService.create(request = body)
    }

    @PostMapping(value = ["/login"], produces = ["application/json"], consumes = ["application/json"])
    fun loginUser(@RequestBody body: UserLoginRequest): ApiSuccessResponse<Any> {
        return userService.auth(body)
    }

    @GetMapping(value = ["/me"])
    fun getAuthUser(auth: Authentication): ApiSuccessResponse<UserAuthPublicResponse> {
        return userService.getUserAuth(auth)
    }

    @GetMapping(value = ["/me/details"])
    fun getAuthUserWithRelation(auth: Authentication): ApiSuccessResponse<UserWithArticle> {
        return userService.getUserWithArticle(auth)
    }

    @PatchMapping(value = ["/me"])
    fun updateProfile(
        auth: Authentication,
        @RequestParam("files") files: MultipartFile,
        @ModelAttribute formData: UpdateProfileRequest
    ): ApiSuccessResponse<UserAuthPublicResponse> {
        return userService.update(auth = auth, files = files, request = formData)
    }

    @GetMapping(value = ["/check-email"])
    fun checkEmail(@RequestParam("email") email: String): ApiSuccessResponse<Any> {
        return userService.checkEmail(email)
    }
}
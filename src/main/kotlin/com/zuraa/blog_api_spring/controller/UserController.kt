package com.zuraa.blog_api_spring.controller

import com.zuraa.blog_api_spring.service.UserService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/")
class UserController(val userService: UserService) {

}
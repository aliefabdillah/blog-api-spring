package com.zuraa.blog_api_spring.controller

import com.zuraa.blog_api_spring.model.ApiErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpClientErrorException.Unauthorized

@RestControllerAdvice
class ErrorController {

    @ExceptionHandler(value = [Unauthorized::class])
    fun unauthorized(): ApiErrorResponse {
        return ApiErrorResponse("Login first before access this endpoint", HttpStatus.UNAUTHORIZED, code = 401)
    }
}
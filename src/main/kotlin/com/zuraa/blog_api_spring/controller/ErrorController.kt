package com.zuraa.blog_api_spring.controller

import com.zuraa.blog_api_spring.model.ApiErrorResponse
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpClientErrorException.Unauthorized
import org.springframework.web.server.ResponseStatusException

@RestControllerAdvice
class ErrorController {

    @ExceptionHandler(value = [Unauthorized::class])
    fun unauthorized(): ApiErrorResponse {
        return ApiErrorResponse(message = "Login first before access this endpoint", status = HttpStatus.UNAUTHORIZED, code = 401)
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(exception: ResponseStatusException): ResponseEntity<Any> {
        return ResponseEntity(exception.body, exception.statusCode)
    }
}
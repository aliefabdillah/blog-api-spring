package com.zuraa.blog_api_spring.utils

import jakarta.validation.Validator
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Component
class ValidationUtil(val validator: Validator) {
    fun validate(requestInput: Any) {
        val result = validator.validate(requestInput)

        // result is array of validation error
        if (result.size > 0) {
            println(result)
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                result.joinToString { it.message }
            )
        }
    }
}
package com.zuraa.blog_api_spring.utils

import jakarta.validation.Validator
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
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

    fun validateImageFile(file: MultipartFile) {
        if (file.contentType?.startsWith("image/") != true) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "File must be an image")
        }

        if (file.size > 5 * 1024 * 1024) { // Limit size to 5MB
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "File size must be less than 5MB")
        }
    }
}
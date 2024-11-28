package com.zuraa.blog_api_spring.service.impl

import com.mongodb.client.model.Collation
import com.zuraa.blog_api_spring.entity.User
import com.zuraa.blog_api_spring.model.ApiSuccessResponse
import com.zuraa.blog_api_spring.model.UserRegisterRequest
import com.zuraa.blog_api_spring.repository.UserRepository
import com.zuraa.blog_api_spring.service.UserService
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.Date

@Service
class UserServiceImpl(val userRepository: UserRepository) : UserService {
    override fun create(request: UserRegisterRequest): ApiSuccessResponse<User> {

        val existingUser = userRepository.findByEmail(request.email)
        if (existingUser != null) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Email already register", null)
        }

//        val hashedPassword = hashUtil.hashBcrypt(request.password)

        val userRequest = User(
            name = request.name,
            email = request.email,
            password = request.password,
            createdAt = Date(),
            updatedAt = Date()
        )

        val usersCreated = userRepository.save(userRequest)

        return ApiSuccessResponse(data = usersCreated, code = 201, status = HttpStatus.CREATED)
    }

}
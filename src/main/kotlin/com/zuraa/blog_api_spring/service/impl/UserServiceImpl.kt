package com.zuraa.blog_api_spring.service.impl

import com.zuraa.blog_api_spring.auth.jwt.JwtProperties
import com.zuraa.blog_api_spring.entity.User
import com.zuraa.blog_api_spring.model.ApiSuccessResponse
import com.zuraa.blog_api_spring.model.UserLoginRequest
import com.zuraa.blog_api_spring.model.UserRegisterRequest
import com.zuraa.blog_api_spring.repository.UserRepository
import com.zuraa.blog_api_spring.service.UserService
import com.zuraa.blog_api_spring.utils.HashUtil
import com.zuraa.blog_api_spring.utils.TokenUtil
import com.zuraa.blog_api_spring.utils.ValidationUtil
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.Date

@Service
class UserServiceImpl(
    val userRepository: UserRepository,
    val hashUtil: HashUtil,
    val validationUtil: ValidationUtil,
    val authManager: AuthenticationManager,
    val userDetailsService: CustomUserServiceDetails,
    val jwtProperties: JwtProperties,
    val tokenUtil: TokenUtil
) : UserService {
    override fun create(request: UserRegisterRequest): ApiSuccessResponse<User> {
        // VALIDATE INPUT
        validationUtil.validate(request)

        //  CHECK EXISTING USER
        val existingUser = userRepository.findByEmail(request.email)
        if (existingUser != null) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Email already register", null)
        }

        // CRYPTO PASSWORD
        val hashedPassword = hashUtil.hashBcrypt(request.password)


        val userRequest = User(
            name = request.name,
            email = request.email,
            password = hashedPassword,
            createdAt = Date(),
            updatedAt = Date()
        )

        // SAVE TO DATABASE
        val usersCreated = userRepository.save(userRequest)

        return ApiSuccessResponse(data = usersCreated, code = 201, status = HttpStatus.CREATED)
    }

    override fun auth(request: UserLoginRequest): ApiSuccessResponse<Any> {
        authManager.authenticate(UsernamePasswordAuthenticationToken(request.email, request.password))

        val user = userDetailsService.loadUserByUsername(request.email)

        val accessToken = createAccessToken(user)

        return ApiSuccessResponse(data = mapOf("token" to accessToken), status = HttpStatus.OK, code = 200)
    }

    private fun createAccessToken(user: UserDetails) = tokenUtil.generate(
        userDetails = user,
        expirationDate = getAccessTokenExpiration()
    )

    private fun getAccessTokenExpiration(): Date =
        Date(System.currentTimeMillis() + jwtProperties.accessTokenExp)

}
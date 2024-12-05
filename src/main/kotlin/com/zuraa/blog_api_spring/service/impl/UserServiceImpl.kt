package com.zuraa.blog_api_spring.service.impl

import com.zuraa.blog_api_spring.auth.jwt.JwtProperties
import com.zuraa.blog_api_spring.entity.User
import com.zuraa.blog_api_spring.model.*
import com.zuraa.blog_api_spring.repository.UserRepository
import com.zuraa.blog_api_spring.service.UserService
import com.zuraa.blog_api_spring.utils.*
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
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
    val tokenUtil: TokenUtil,
    val fileStorageService: FileStorageService
) : UserService {

    override fun create(request: UserRegisterRequest): ApiSuccessResponse<UserAuthPublicResponse> {
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
            imgProfile = request.imgProfile ?: "",
            createdAt = Date(),
            updatedAt = Date()
        )

        // SAVE TO DATABASE
        val usersCreated = userRepository.save(userRequest).toUserPublicResponse()

        return ApiSuccessResponse(data = usersCreated, code = 201, status = HttpStatus.CREATED)
    }

    override fun auth(request: UserLoginRequest): ApiSuccessResponse<Any> {

        try {
            // validate email and password with data in database
            authManager.authenticate(UsernamePasswordAuthenticationToken(request.email, request.password))
        } catch (e: BadCredentialsException) {
            // return exception if bad credentials
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Wrong password!",
                null
            )
        }

        val user = userDetailsService.loadUserByUsername(request.email)

        val accessToken = createAccessToken(user)

        return ApiSuccessResponse(data = mapOf("token" to accessToken), status = HttpStatus.OK, code = 200)
    }

    override fun getUserAuth(auth: Authentication): ApiSuccessResponse<UserAuthPublicResponse> {
        // Get User data by email authenticated user
        val userData = userRepository.findByEmail(auth.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "User not found!"
        )

        return ApiSuccessResponse(data = userData.toUserPublicResponse(), code = 200, status = HttpStatus.OK)
    }

    override fun getUserWithArticle(auth: Authentication): ApiSuccessResponse<UserWithArticle> {
        val userData = userRepository.findByEmail(auth.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "User not found!"
        )

        return ApiSuccessResponse(data = userData.toUserWithRelationResponse(), code = 200, status = HttpStatus.OK)
    }

    override fun update(
        auth: Authentication,
        request: UpdateProfileRequest,
        files: MultipartFile
    ): ApiSuccessResponse<UserAuthPublicResponse> {

        validationUtil.validate(request)

        val updatedData = userRepository.findByEmail(auth.name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "User not found!"
        )

        updatedData.name = request.name ?: updatedData.name

        if (!files.isEmpty) {
            try {
                validationUtil.validateImageFile(files)
                val filePath = fileStorageService.storeFile(files)
                updatedData.imgProfile = filePath
            } catch (e: Exception) {
                throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
            }
        }

        userRepository.save(updatedData)

        return ApiSuccessResponse(data = updatedData.toUserPublicResponse(), code = 200, status = HttpStatus.OK)
    }

    override fun checkEmail(email: String): ApiSuccessResponse<Any> {
        val userData = userRepository.findByEmail(email)
        return ApiSuccessResponse(data = userData ?: true, code = 200, status = HttpStatus.OK)
    }

    private fun createAccessToken(user: UserDetails) = tokenUtil.generate(
        userDetails = user,
        expirationDate = getAccessTokenExpiration()
    )

    private fun getAccessTokenExpiration(): Date =
        Date(System.currentTimeMillis() + jwtProperties.accessTokenExp)
}
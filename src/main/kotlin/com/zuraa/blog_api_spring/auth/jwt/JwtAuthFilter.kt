package com.zuraa.blog_api_spring.auth.jwt

import com.zuraa.blog_api_spring.service.impl.CustomUserServiceDetails
import com.zuraa.blog_api_spring.utils.TokenUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.server.ResponseStatusException

@Component
class JwtAuthFilter(private val userDetailsService: CustomUserServiceDetails, private val tokenUtil: TokenUtil) :
    OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val requestURI = request.requestURI
        val requestMethod = request.method

        // Check if the endpoint is public
        if (isPublicEndpoint(requestURI, requestMethod)) {
            filterChain.doFilter(request, response) // Proceed without authentication
            return
        }

        try {
            val authHeader: String? = request.getHeader("Authorization") ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Token")

            if (authHeader.doesNotContainBearerToken()) {
                filterChain.doFilter(request, response)
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Token")
            }

            val jwtToken = authHeader!!.extractTokenValue()
            val email = tokenUtil.extractEmail(jwtToken)

            if (email != null && SecurityContextHolder.getContext().authentication == null) {
                val foundUser = userDetailsService.loadUserByUsername(email)
                if (tokenUtil.isValid(jwtToken, foundUser))
                    updateContext(foundUser, request)
                filterChain.doFilter(request, response)
            }
        } catch (ex: ResponseStatusException) {
            // Handle the exception and return a custom response body
            response.status = ex.statusCode.value()
            response.contentType = "application/json"
            response.writer.write(
                """{
                "message": "${ex.reason}",
                "status": ${ex.statusCode.value()}
            }"""
            )
        }
    }

    // Helper function to check public endpoints
    private fun isPublicEndpoint(requestURI: String, requestMethod: String): Boolean {
        val publicEndpoints = listOf(
            "POST" to "/api/user/register",  // Example public endpoints
            "POST" to "/api/user/login",
            "GET" to "/api/article/"
        )

        return publicEndpoints.any { (method, route) ->
            requestMethod.equals(method, ignoreCase = true) && requestURI.startsWith(route)
        }
    }

    private fun String?.doesNotContainBearerToken() =
        this == null || !this.startsWith("Bearer ")

    private fun String.extractTokenValue() =
        this.substringAfter("Bearer ")

    private fun updateContext(foundUser: UserDetails, request: HttpServletRequest) {
        val authToken = UsernamePasswordAuthenticationToken(foundUser, null, foundUser.authorities)
        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authToken
    }

}
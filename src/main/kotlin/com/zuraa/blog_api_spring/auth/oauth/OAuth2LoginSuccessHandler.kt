package com.zuraa.blog_api_spring.auth.oauth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.web.server.ResponseStatusException

class OAuth2LoginSuccessHandler : AuthenticationSuccessHandler {

    private val restTemplate = RestTemplate()
    private val objectMapper = ObjectMapper()

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {

        val oAuthUser = authentication.principal as DefaultOAuth2User
        val email = oAuthUser.attributes["email"] ?: oAuthUser.attributes["blog"]
        val name = oAuthUser.attributes["name"] as String
        val imgProfile = oAuthUser.attributes["avatar_url"] as String
        val password = oAuthUser.attributes["id"] as Int

        val emailExists = checkEmailExists(email as String)

        if (!emailExists) {
            val registerResponse = callRegisterEndpoint(name, email as String, password.toString(), imgProfile)

            if (registerResponse.statusCode != HttpStatus.OK || registerResponse.statusCode == HttpStatus.CONFLICT) {
                throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Registration failed: ${registerResponse.body}"
                )
            }
        }

        val loginResponse = callLoginEndpoint(email, password.toString())

        if (loginResponse.statusCode != HttpStatus.OK) throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Registration failed: ${loginResponse.body}"
        )

        val jsonResponse: JsonNode = objectMapper.readTree(loginResponse.body)
        val accessToken = jsonResponse.get("data").get("token").asText()

        if (accessToken != null) {
            // Send the access token to the frontend as a JSON response
            response.contentType = "application/json"
            response.writer.write("{\"token\": \"$accessToken\", \"token_type\":\"Bearer\"}")
            response.writer.flush()
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Failed to retrieve access token")
        }
    }

    private fun callRegisterEndpoint(
        name: String,
        email: String,
        password: String,
        imgProfile: String
    ): ResponseEntity<String> {
        val url = "http://localhost:8080/api/user/register"

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val requestBody = mapOf(
            "name" to name,
            "email" to email,
            "password" to password,
            "imgProfile" to imgProfile
        )

        val requestEntity = HttpEntity(requestBody, headers)

        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String::class.java)
    }

    private fun callLoginEndpoint(email: String, password: String): ResponseEntity<String> {
        val url = "http://localhost:8080/api/user/login"

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val requestBody = mapOf(
            "email" to email,
            "password" to password,
        )

        val requestEntity = HttpEntity(requestBody, headers)

        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String::class.java)
    }

    private fun checkEmailExists(email: String): Boolean {
        val url = UriComponentsBuilder
            .fromHttpUrl("http://localhost:8080/api/user/check-email")
            .queryParam("email", email)
            .toUriString()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val requestEntity = HttpEntity<String>(headers)

        val response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String::class.java)
        return response.statusCode == HttpStatus.OK && response.body?.contains("data") == true
    }
}

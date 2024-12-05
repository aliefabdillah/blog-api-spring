package com.zuraa.blog_api_spring.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class HomeController(
    private val authorizedClientService: OAuth2AuthorizedClientService
) {

    @GetMapping("/index")
    fun index(): String {
        return "index"
    }

    @GetMapping("/home")
    fun home(model: Model): String {
        return "home"
    }
}
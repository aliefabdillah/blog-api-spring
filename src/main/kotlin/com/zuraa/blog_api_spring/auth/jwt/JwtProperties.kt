package com.zuraa.blog_api_spring.auth.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val key: String,
    val accessTokenExp: Long,
    val refreshTokenExp: Long
)

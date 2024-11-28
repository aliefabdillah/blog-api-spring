package com.zuraa.blog_api_spring

import com.zuraa.blog_api_spring.auth.jwt.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties::class)
class BlogApiSpringApplication

fun main(args: Array<String>) {
	runApplication<BlogApiSpringApplication>(*args)
}

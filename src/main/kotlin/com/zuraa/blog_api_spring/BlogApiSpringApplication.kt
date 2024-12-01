package com.zuraa.blog_api_spring

import com.zuraa.blog_api_spring.auth.jwt.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties::class)
@EnableCaching 	//annotation to make app can use cache
class BlogApiSpringApplication

fun main(args: Array<String>) {
	runApplication<BlogApiSpringApplication>(*args)
}

package com.zuraa.blog_api_spring

import com.zuraa.blog_api_spring.auth.jwt.JwtProperties
import io.github.cdimascio.dotenv.Dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties::class)
@EnableCaching 	//annotation to make app can use cache
class BlogApiSpringApplication

fun main(args: Array<String>) {

	// Load .env file
	val dotenv = Dotenv.configure().load()

	// Set the environment variables
	dotenv.entries().forEach { entry ->
		System.setProperty(entry.key, entry.value)
	}

	runApplication<BlogApiSpringApplication>(*args)
}

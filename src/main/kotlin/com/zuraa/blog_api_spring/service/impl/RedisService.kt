package com.zuraa.blog_api_spring.service.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisService(private val redisTemplate: RedisTemplate<String, String>) {
    val objectMapper = ObjectMapper().registerKotlinModule()

    // Save data to Redis with the specified key
    fun <T> saveResponse(key: String, value: T) {
        val jsonData = objectMapper.writeValueAsString(value)
        redisTemplate.opsForValue().set(key, jsonData, Duration.ofMinutes(10))
    }

    // Get data from Redis by key
    fun <T> getResponse(key: String, typeReference: TypeReference<T>): T? {
        val jsonData = redisTemplate.opsForValue().get(key)
        if (jsonData != null) {
            return objectMapper.readValue(jsonData, typeReference)
        }
        return null
    }

    // Delete data from Redis by key
    fun deleteResponse(key: String) {
        redisTemplate.delete(key)
    }
}
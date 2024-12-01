package com.zuraa.blog_api_spring.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.zuraa.blog_api_spring.model.ArticleWithAuthor
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RedisService(private val redisTemplate: RedisTemplate<String, Any>) {
    val objectMapper = ObjectMapper()

//    fun saveResponse(key: String, response: MutableList<ArticleWithAuthor>) {
//        val jsonData = objectMapper.writeValueAsString(response)
//        redisTemplate.opsForValue().set(key, jsonData)
//    }
//
//    fun getResponse(key: String): MutableList<ArticleWithAuthor>? {
//        val jsonData = redisTemplate.opsForValue().get(key) ?: throw ResponseStatusException(
//            HttpStatus.NOT_FOUND,
//            "Article Not Found in cache"
//        )
//
//        return objectMapper.readValue<MutableList<ArticleWithAuthor>>(jsonData)
//    }

    // Save data to Redis with the specified key
    fun <T : Any> saveResponse(key: String, value: T) {
//        val jsonData = objectMapper.writeValue(value)
        redisTemplate.opsForValue().set(key, value)
    }

    // Get data from Redis by key
    fun <T> getResponse(key: String): T? {
        val jsonData = redisTemplate.opsForValue().get(key)
        return jsonData as T
    }

    // Delete data from Redis by key
    fun deleteResponse(key: String) {
        redisTemplate.delete(key)
    }
}
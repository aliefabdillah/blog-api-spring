package com.zuraa.blog_api_spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {
    //    FUNCTION FOR CONFIG REDIS
    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, String> {
        val redisTemplate = RedisTemplate<String, String>()
        redisTemplate.connectionFactory = redisConnectionFactory

        // Set serializers for the RedisTemplate
        val stringSerializer = StringRedisSerializer()
        val jsonSerializer = Jackson2JsonRedisSerializer(Any::class.java)

        redisTemplate.keySerializer = stringSerializer
        redisTemplate.valueSerializer = jsonSerializer
        redisTemplate.hashKeySerializer = stringSerializer
        redisTemplate.hashValueSerializer = jsonSerializer

        return redisTemplate
    }
}
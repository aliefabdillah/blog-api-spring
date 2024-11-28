package com.zuraa.blog_api_spring.repository

import com.zuraa.blog_api_spring.entity.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface UserRepository : MongoRepository<User, String> {
    @Query("{'email': ?0}")
    fun findByEmail(email: String): User?
}
package com.zuraa.blog_api_spring.repository

import com.zuraa.blog_api_spring.entity.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.Optional

interface UserRepository : MongoRepository<User, String> {
    @Query("{'email': ?0}")
    fun findByEmail(email: String?): User?

    @Query("{ 'name': { \$regex: ?0, \$options: 'i' }}")
    fun findByName(name: String?): List<User>
}
package com.zuraa.blog_api_spring.repository

import com.zuraa.blog_api_spring.entity.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {
}
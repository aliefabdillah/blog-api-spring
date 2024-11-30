package com.zuraa.blog_api_spring.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date
import java.util.UUID

@Document(collection = "users")
data class User(
    @Id
    val id: String = UUID.randomUUID().toString(),

    var name: String,

    @Indexed(unique = true)
    var email: String,

    val password: String,

    var imgProfile: String? = null,

    val createdAt: Date,

    val updatedAt: Date,

    @DBRef
    val articles: MutableList<Article> = mutableListOf()
)

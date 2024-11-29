package com.zuraa.blog_api_spring.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "articles")
data class Article(
    @Id
    val id: String = UUID.randomUUID().toString(),

    val title: String,

    val content: String,

    val authorId: String
)

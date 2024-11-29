package com.zuraa.blog_api_spring.repository

import com.zuraa.blog_api_spring.entity.Article
import org.springframework.data.mongodb.repository.MongoRepository

interface ArticleRepository : MongoRepository<Article, String> {
}
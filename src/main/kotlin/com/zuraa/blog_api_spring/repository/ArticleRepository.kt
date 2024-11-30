package com.zuraa.blog_api_spring.repository

import com.zuraa.blog_api_spring.entity.Article
import com.zuraa.blog_api_spring.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface ArticleRepository : MongoRepository<Article, String> {
    @Query("{ \$and: [ { 'title': { \$regex: ?0, \$options: 'i' } }, { 'authorId': { \$in: ?1 } } ] }")
    fun findByTitleAndAuthorId(title: String, authorIds: List<String>, pageable: Pageable): Page<Article>

}
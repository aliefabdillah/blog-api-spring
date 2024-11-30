package com.zuraa.blog_api_spring.service

import com.zuraa.blog_api_spring.entity.Article
import com.zuraa.blog_api_spring.model.*
import org.springframework.security.core.Authentication

interface ArticleService {
    fun create(auth: Authentication, createRequest: CreateArticleRequest): ApiSuccessResponse<ArticleWithAuthor>
    fun getAll(query: ListArticleQuery): ApiSuccessResponse<List<ArticleWithAuthor>>
    fun getById(id: String): ApiSuccessResponse<ArticleWithAuthor>
    fun update(id: String, updateRequest: UpdateArticleRequest): ApiSuccessResponse<Article>
    fun delete(id: String): ApiSuccessResponse<Any>
}
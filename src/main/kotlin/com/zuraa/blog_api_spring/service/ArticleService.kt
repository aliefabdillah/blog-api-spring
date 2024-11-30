package com.zuraa.blog_api_spring.service

import com.zuraa.blog_api_spring.entity.Article
import com.zuraa.blog_api_spring.model.ApiSuccessResponse
import com.zuraa.blog_api_spring.model.ArticleWithAuthor
import com.zuraa.blog_api_spring.model.CreateArticleRequest
import com.zuraa.blog_api_spring.model.UpdateArticleRequest
import org.springframework.security.core.Authentication

interface ArticleService {
    fun create(auth: Authentication, createRequest: CreateArticleRequest): ApiSuccessResponse<ArticleWithAuthor>
    fun getAll(query: Any): ApiSuccessResponse<List<Article>>
    fun getById(id: String): ApiSuccessResponse<ArticleWithAuthor>
    fun update(id: String, updateRequest: UpdateArticleRequest): ApiSuccessResponse<Article>
    fun delete(id: String): ApiSuccessResponse<Any>
}
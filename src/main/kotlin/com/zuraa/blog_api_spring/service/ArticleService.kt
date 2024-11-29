package com.zuraa.blog_api_spring.service

import com.zuraa.blog_api_spring.entity.Article
import com.zuraa.blog_api_spring.model.ApiSuccessResponse
import com.zuraa.blog_api_spring.model.CreateArticleRequest
import com.zuraa.blog_api_spring.model.UpdateArticleRequest

interface ArticleService {
    fun create(createRequest: CreateArticleRequest): ApiSuccessResponse<Article>
    fun getAll(query: Any): ApiSuccessResponse<List<Article>>
    fun getById(id: String): ApiSuccessResponse<Article>
    fun update(id: String, updateRequest: UpdateArticleRequest): ApiSuccessResponse<Article>
    fun delete(id: String): ApiSuccessResponse<Any>
}
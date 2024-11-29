package com.zuraa.blog_api_spring.service.impl

import com.zuraa.blog_api_spring.entity.Article
import com.zuraa.blog_api_spring.model.ApiSuccessResponse
import com.zuraa.blog_api_spring.model.CreateArticleRequest
import com.zuraa.blog_api_spring.model.UpdateArticleRequest
import com.zuraa.blog_api_spring.service.ArticleService
import org.springframework.stereotype.Service

@Service
class ArticleServiceImpl : ArticleService {
    override fun create(createRequest: CreateArticleRequest): ApiSuccessResponse<Article> {
        TODO("Not yet implemented")
    }

    override fun getAll(query: Any): ApiSuccessResponse<List<Article>> {
        TODO("Not yet implemented")
    }

    override fun getById(id: String): ApiSuccessResponse<Article> {
        TODO("Not yet implemented")
    }

    override fun update(id: String, updateRequest: UpdateArticleRequest): ApiSuccessResponse<Article> {
        TODO("Not yet implemented")
    }

    override fun delete(id: String): ApiSuccessResponse<Any> {
        TODO("Not yet implemented")
    }
}
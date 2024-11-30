package com.zuraa.blog_api_spring.controller

import com.zuraa.blog_api_spring.model.ApiSuccessResponse
import com.zuraa.blog_api_spring.model.ArticleWithAuthor
import com.zuraa.blog_api_spring.model.CreateArticleRequest
import com.zuraa.blog_api_spring.service.ArticleService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ArticleController(val articleService: ArticleService) {

    @PostMapping(value = ["/api/article/"])
    fun createArticle(
        authentication: Authentication,
        @RequestBody createArticleRequest: CreateArticleRequest
    ): ApiSuccessResponse<ArticleWithAuthor> {
        return articleService.create(authentication, createArticleRequest)
    }
}
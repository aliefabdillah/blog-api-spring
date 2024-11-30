package com.zuraa.blog_api_spring.controller

import com.zuraa.blog_api_spring.entity.Article
import com.zuraa.blog_api_spring.model.*
import com.zuraa.blog_api_spring.service.ArticleService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/article")
class ArticleController(val articleService: ArticleService) {

    @PostMapping(value = ["/"])
    fun createArticle(
        authentication: Authentication,
        @RequestBody createArticleRequest: CreateArticleRequest
    ): ApiSuccessResponse<ArticleWithAuthor> {
        return articleService.create(authentication, createArticleRequest)
    }

    @GetMapping(value = ["/{id}"])
    fun getArticleById(
        @PathVariable("id") id: String
    ): ApiSuccessResponse<ArticleWithAuthor> {
        return articleService.getById(id)
    }

    @GetMapping(value = ["/list"])
    fun getListArticle(
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("page", defaultValue = "1") page: Int,
        @RequestParam("title", defaultValue = "") title: String,
        @RequestParam("authorName", defaultValue = "") authorName: String,
    ): ApiSuccessResponse<List<ArticleWithAuthor>> {
        val query = ListArticleQuery(
            size, page - 1, title, authorName
        )
        return articleService.getAll(query)
    }

    @PatchMapping(value = ["/{id}"])
    fun updateArticle(
        @RequestBody body: UpdateArticleRequest,
        @PathVariable("id") id: String
    ): ApiSuccessResponse<Article> {
        return articleService.update(id, body)
    }
}
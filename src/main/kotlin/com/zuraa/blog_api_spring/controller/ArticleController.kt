package com.zuraa.blog_api_spring.controller

import com.zuraa.blog_api_spring.model.ApiSuccessResponse
import com.zuraa.blog_api_spring.model.ArticleWithAuthor
import com.zuraa.blog_api_spring.model.CreateArticleRequest
import com.zuraa.blog_api_spring.model.ListArticleQuery
import com.zuraa.blog_api_spring.service.ArticleService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
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
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("title", defaultValue = "") title: String,
        @RequestParam("authorName", defaultValue = "") authorName: String,
    ) : ApiSuccessResponse<List<ArticleWithAuthor>> {
        val query = ListArticleQuery(
            size, page -  1, title, authorName
        )
        return articleService.getAll(query)
    }
}
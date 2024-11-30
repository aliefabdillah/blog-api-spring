package com.zuraa.blog_api_spring.service.impl

import com.zuraa.blog_api_spring.entity.Article
import com.zuraa.blog_api_spring.model.ApiSuccessResponse
import com.zuraa.blog_api_spring.model.ArticleWithAuthor
import com.zuraa.blog_api_spring.model.CreateArticleRequest
import com.zuraa.blog_api_spring.model.UpdateArticleRequest
import com.zuraa.blog_api_spring.repository.ArticleRepository
import com.zuraa.blog_api_spring.repository.UserRepository
import com.zuraa.blog_api_spring.service.ArticleService
import com.zuraa.blog_api_spring.utils.ValidationUtil
import com.zuraa.blog_api_spring.utils.toUserPublicResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.Date

@Service
class ArticleServiceImpl(
    val validationUtil: ValidationUtil,
    val userRepository: UserRepository,
    val articleRepository: ArticleRepository
) : ArticleService {
    override fun create(auth: Authentication, createRequest: CreateArticleRequest): ApiSuccessResponse<ArticleWithAuthor> {
        validationUtil.validate(createRequest)

        // get user
        val authUser =
            userRepository.findByEmail(auth.name) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not login/authenticated"
            )

        val article = Article(
            title = createRequest.title,
            content = createRequest.content,
            authorId = authUser.id,
            createdAt = Date(),
            updatedAt = Date()
        )

        articleRepository.save(article)

        authUser.articles.add(article)

        userRepository.save(authUser)

        val articleResponse = ArticleWithAuthor(
            id = article.id,
            title = article.title,
            content = article.content,
            authorId = authUser.id,
            author = authUser.toUserPublicResponse(),
            createdAt = article.createdAt,
            updatedAt = article.updatedAt
        )

        return ApiSuccessResponse(data = articleResponse, status = HttpStatus.CREATED, code = 201)
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
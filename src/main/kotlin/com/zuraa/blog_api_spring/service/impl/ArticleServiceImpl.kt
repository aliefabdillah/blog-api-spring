package com.zuraa.blog_api_spring.service.impl

import com.zuraa.blog_api_spring.entity.Article
import com.zuraa.blog_api_spring.entity.User
import com.zuraa.blog_api_spring.model.*
import com.zuraa.blog_api_spring.repository.ArticleRepository
import com.zuraa.blog_api_spring.repository.UserRepository
import com.zuraa.blog_api_spring.service.ArticleService
import com.zuraa.blog_api_spring.utils.ValidationUtil
import com.zuraa.blog_api_spring.utils.toUserPublicResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
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
    override fun create(
        auth: Authentication,
        createRequest: CreateArticleRequest
    ): ApiSuccessResponse<ArticleWithAuthor> {
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

        val articleResponse = toArticleWithAuthorResponse(authUser, article)

        return ApiSuccessResponse(data = articleResponse, status = HttpStatus.CREATED, code = 201)
    }

    override fun getAll(query: ListArticleQuery): ApiSuccessResponse<List<ArticleWithAuthor>> {
        // Validate query
        validationUtil.validate(query)

        val responseData = mutableListOf<ArticleWithAuthor>()
        val pageable = PageRequest.of(query.page, query.size)

        //  GET USER LIST BY QUERY NAME
        val usersByName = userRepository.findByName(query.authorName)
        // CONVERT INTO LIST OF ID USER
        val listOfIdUser = usersByName.map { it.id }

        // GET Articles BY Title AND AuthorId
        val articlesPageData = articleRepository.findByTitleAndAuthorId(query.title, listOfIdUser, pageable)

        // Return empty body
        if (articlesPageData.isEmpty) {
            return ApiSuccessResponse(data = responseData, status = HttpStatus.OK, code = 200)
        }

        // get author data
        articlesPageData.content.forEach { article ->
            val authorData = userRepository.findByIdOrNull(article.authorId) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found!"
            )

            responseData.add(toArticleWithAuthorResponse(authorData, article))
        }

        val paginationData = Pagination(
            current = query.page + 1,
            perPage = query.size,
            lastPage = articlesPageData.totalPages,
            total = articlesPageData.content.size
        )

        return ApiSuccessResponse(data = responseData, status = HttpStatus.OK, code = 200, pagination = paginationData)
    }

    override fun getById(id: String): ApiSuccessResponse<ArticleWithAuthor> {
        val articleData = articleRepository.findByIdOrNull(id) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Article not found!"
        )

        val authUser =
            userRepository.findByIdOrNull(articleData.authorId) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found!"
            )

        val responseData = toArticleWithAuthorResponse(authUser, articleData)
        return ApiSuccessResponse(status = HttpStatus.OK, code = 200, data = responseData)
    }

    override fun update(id: String, updateRequest: UpdateArticleRequest): ApiSuccessResponse<Article> {
        validationUtil.validate(updateRequest)

        val updatedData = articleRepository.findByIdOrNull(id) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Article not found!"
        )

        updatedData.title = updateRequest.title ?: updatedData.title
        updatedData.content = updateRequest.content ?: updatedData.content

        articleRepository.save(updatedData)

        return ApiSuccessResponse(data = updatedData, status = HttpStatus.OK, code = 200)
    }

    override fun delete(id: String): ApiSuccessResponse<Any> {
        val deletedUser = articleRepository.findByIdOrNull(id) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Article  not found!"
        )

        articleRepository.delete(deletedUser)

        return ApiSuccessResponse(data = null, status = HttpStatus.OK, code = 200)
    }

    private fun toArticleWithAuthorResponse(user: User, article: Article) =
        ArticleWithAuthor(
            id = article.id,
            title = article.title,
            content = article.content,
            authorId = user.id,
            author = user.toUserPublicResponse(),
            createdAt = article.createdAt,
            updatedAt = article.updatedAt
        )
}
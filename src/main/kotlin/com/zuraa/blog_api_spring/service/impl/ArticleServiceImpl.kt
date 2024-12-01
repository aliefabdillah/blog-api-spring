package com.zuraa.blog_api_spring.service.impl

import com.zuraa.blog_api_spring.entity.Article
import com.zuraa.blog_api_spring.entity.User
import com.zuraa.blog_api_spring.model.*
import com.zuraa.blog_api_spring.repository.ArticleRepository
import com.zuraa.blog_api_spring.repository.UserRepository
import com.zuraa.blog_api_spring.service.ArticleService
import com.zuraa.blog_api_spring.utils.ValidationUtil
import com.zuraa.blog_api_spring.utils.toUserPublicResponse
import org.springframework.cache.annotation.CacheEvict
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.util.Date

@Service
class ArticleServiceImpl(
    val validationUtil: ValidationUtil,
    val userRepository: UserRepository,
    val articleRepository: ArticleRepository,
    val fileStorageService: FileStorageService,
    val redisService: RedisService
) : ArticleService {
    override fun create(
        auth: Authentication,
        files: MultipartFile?,
        createRequest: CreateArticleRequest
    ): ApiSuccessResponse<ArticleWithAuthor> {
        validationUtil.validate(createRequest)

        // get user
        val authUser =
            userRepository.findByEmail(auth.name) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not login/authenticated"
            )

        var filePath = ""
        if (files != null) {
            if (!files.isEmpty) {
                try {
                    validationUtil.validateImageFile(files)
                    filePath = fileStorageService.storeFile(files)
                } catch (e: Exception) {
                    throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
                }
            }
        }

        val article = Article(
            title = createRequest.title,
            content = createRequest.content,
            imgCover = filePath,
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

    //    @Cacheable(value = ["articles"], key = "'all_article'")
    override fun getAll(query: ListArticleQuery): ApiSuccessResponse<List<ArticleWithAuthor>> {
        val responseData = mutableListOf<ArticleWithAuthor>()
        val cacheKey = "articles:all_articles"

        // Validate query
        validationUtil.validate(query)

        val cachedResponse = redisService.getResponse<List<ArticleWithAuthor>>(cacheKey)

        if (cachedResponse != null) {
            val paginatedData = paginateData(cachedResponse, size = query.size, page = query.page)

            return ApiSuccessResponse(
                data = paginatedData,
                status = HttpStatus.OK,
                code = 200,
                pagination = Pagination(
                    current = query.page + 1,
                    perPage = query.size,
                    lastPage = (cachedResponse.size + query.size - 1) / query.size,
                    total = cachedResponse.size
                )
            )
        }

        //  GET USER LIST BY QUERY NAME
        val usersByName = userRepository.findByName(query.authorName)
//        val listOfIdUser = usersByName.map { it.id }

        // GET Articles BY Title AND AuthorId
        val articlesPageData = articleRepository.findAll()

        // Return empty body
        if (articlesPageData.size == 0) {
            return ApiSuccessResponse(data = responseData, status = HttpStatus.OK, code = 200)
        }

        val paginatedData = paginateData(articlesPageData, query.page, query.size)

        val paginationData = Pagination(
            current = query.page + 1,
            perPage = query.size,
            lastPage = (articlesPageData.size + query.size - 1) / query.size,
            total = articlesPageData.size
        )

        // get author data
        paginatedData.forEach { article ->
            val authorData = userRepository.findByIdOrNull(article.authorId) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found!"
            )

            responseData.add(toArticleWithAuthorResponse(authorData, article))
        }


        // SAVE INTO REDIS
        redisService.saveResponse(cacheKey, responseData)

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

    override fun update(
        id: String,
        files: MultipartFile?,
        updateRequest: UpdateArticleRequest
    ): ApiSuccessResponse<Article> {
        validationUtil.validate(updateRequest)

        val updatedData = articleRepository.findByIdOrNull(id) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Article not found!"
        )

        updatedData.title = updateRequest.title ?: updatedData.title
        updatedData.content = updateRequest.content ?: updatedData.content

        if (files != null) {
            if (!files.isEmpty) {
                try {
                    validationUtil.validateImageFile(files)
                    val filePath = fileStorageService.storeFile(files)
                    updatedData.imgCover = filePath
                } catch (e: Exception) {
                    throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
                }
            }
        }

        articleRepository.save(updatedData)

        return ApiSuccessResponse(data = updatedData, status = HttpStatus.OK, code = 200)
    }

    override fun delete(id: String): ApiSuccessResponse<Any> {
        val deletedArticle = articleRepository.findByIdOrNull(id) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Article  not found!"
        )

        articleRepository.delete(deletedArticle)

        return ApiSuccessResponse(data = null, status = HttpStatus.OK, code = 200)
    }

    private fun toArticleWithAuthorResponse(user: User, article: Article) =
        ArticleWithAuthor(
            id = article.id,
            title = article.title,
            content = article.content,
            imgProfile = article.imgCover ?: "",
            authorId = user.id,
            author = user.toUserPublicResponse(),
            createdAt = article.createdAt,
            updatedAt = article.updatedAt
        )

    private fun <T> paginateData(data: List<T>, page: Int, size: Int): List<T> {
        val totalItems = data.size
        val totalPages = (totalItems + size - 1) / size

        val currentPage = when {
            page < 1 -> 1
            page > totalPages -> totalPages
            else -> page
        }

        val startIndex = (currentPage - 1) * size
        val endIndex = (startIndex + size).coerceAtMost(totalItems)

        return data.subList(startIndex, endIndex)
    }
}
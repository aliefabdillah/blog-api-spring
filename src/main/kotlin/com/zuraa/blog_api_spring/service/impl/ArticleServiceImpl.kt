package com.zuraa.blog_api_spring.service.impl

import com.fasterxml.jackson.core.type.TypeReference
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

    private val cacheKey = "articles:all_articles"

    override fun create(
        auth: Authentication,
        files: MultipartFile?,
        createRequest: CreateArticleRequest
    ): ApiSuccessResponse<ArticleWithAuthor> {

        validationUtil.validate(createRequest)  // Validate create request

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

        articleRepository.save(article)                 //save new article data
        authUser.articles.add(article)                  //connect new article to authenticated user
        userRepository.save(authUser)                   //save updated user data
        redisService.deleteResponse(this.cacheKey)      //delete existing redis cached data

        val articleResponse = toArticleWithAuthorResponse(authUser, article)

        return ApiSuccessResponse(data = articleResponse, status = HttpStatus.CREATED, code = 201)
    }

    override fun getAll(query: ListArticleQuery): ApiSuccessResponse<List<ArticleWithAuthor>> {
        val responseData = mutableListOf<ArticleWithAuthor>()

        validationUtil.validate(query)  // Validate query

        //  GET FROM CACHE
        val cachedResponse = redisService.getResponse(
            this.cacheKey,
            object : TypeReference<List<Article>>() {})

        if (cachedResponse != null) {
            val filteredCachedResponse = filterData(cachedResponse, query)

            val paginatedData = paginateData(filteredCachedResponse, size = query.size, page = query.page)

            // get author data
            paginatedData.forEach { article ->
                responseData.add(getAuthor(article))
            }

            return ApiSuccessResponse(
                data = responseData,
                status = HttpStatus.OK,
                code = 200,
                pagination = Pagination(
                    current = query.page,
                    perPage = query.size,
                    lastPage = (filteredCachedResponse.size + query.size - 1) / query.size,
                    total = filteredCachedResponse.size
                )
            )
        }

        // GET Articles BY Title AND AuthorId
        val articlesData = articleRepository.findAll()

        // SAVE INTO REDIS
        redisService.saveResponse(this.cacheKey, articlesData)

        // filter by title and author id
        val filteredArticleData = filterData(articlesData, query)
        // PAGINATE DATA
        val paginatedData = paginateData(filteredArticleData, query.page, query.size)

        val paginationData = Pagination(
            current = query.page,
            perPage = query.size,
            lastPage = (filteredArticleData.size + query.size - 1) / query.size,
            total = filteredArticleData.size
        )

        // get author data
        paginatedData.forEach { article ->
            responseData.add(getAuthor(article))
        }

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
        validationUtil.validate(updateRequest)  //validate request user

        // GET updated article data by id
        val updatedData = articleRepository.findByIdOrNull(id) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Article not found!"
        )

        // update tittle and content
        updatedData.title = updateRequest.title ?: updatedData.title
        updatedData.content = updateRequest.content ?: updatedData.content

        // update file
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

        articleRepository.save(updatedData)         //save updated article data to db
        redisService.deleteResponse(this.cacheKey)  //delete existing cached data in redis

        return ApiSuccessResponse(data = updatedData, status = HttpStatus.OK, code = 200)
    }

    override fun delete(id: String): ApiSuccessResponse<Any> {
        val deletedArticle = articleRepository.findByIdOrNull(id) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Article  not found!"
        )

        articleRepository.delete(deletedArticle)        //delete data from database
        redisService.deleteResponse(this.cacheKey)      //delete cached data in redis

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
            page > totalPages -> return emptyList()
            else -> page
        }

        val startIndex = (currentPage - 1) * size
        val endIndex = (startIndex + size).coerceAtMost(totalItems)

        return data.subList(startIndex, endIndex)
    }

    private fun filterData(data: List<Article>, query: ListArticleQuery): List<Article> {
        //  GET USER LIST BY QUERY NAME
        val usersByName = userRepository.findByName(query.authorName)
        val listOfIdUser = usersByName.map { it.id }

        return data.filter {
            (it.title.contains(query.title, ignoreCase = true)) &&
                    (it.authorId in listOfIdUser)
        }
    }

    private fun getAuthor(data: Article): ArticleWithAuthor {
        val authorData = userRepository.findByIdOrNull(data.authorId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "User not found!"
        )

        return toArticleWithAuthorResponse(authorData, data)
    }
}
package com.zuraa.blog_api_spring.utils

import com.zuraa.blog_api_spring.entity.User
import com.zuraa.blog_api_spring.model.UserAuthPublicResponse
import com.zuraa.blog_api_spring.model.UserWithArticle

fun User.toUserPublicResponse(): UserAuthPublicResponse =
    UserAuthPublicResponse(
        id = this.id,
        name = this.name,
        email = this.email,
        imgProfile = this.imgProfile ?: "",
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
    )

fun User.toUserWithRelationResponse(): UserWithArticle =
    UserWithArticle(
        id = this.id,
        name = this.name,
        email = this.email,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        articles = this.articles
    )
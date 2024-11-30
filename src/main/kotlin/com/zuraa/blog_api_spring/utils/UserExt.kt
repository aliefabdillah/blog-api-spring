package com.zuraa.blog_api_spring.utils

import com.zuraa.blog_api_spring.entity.User
import com.zuraa.blog_api_spring.model.UserPublicResponse

fun User.toUserPublicResponse(): UserPublicResponse =
    UserPublicResponse(
        id = this.id,
        name = this.name,
        email = this.email,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
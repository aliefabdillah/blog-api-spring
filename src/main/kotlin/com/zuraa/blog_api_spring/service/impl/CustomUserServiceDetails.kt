package com.zuraa.blog_api_spring.service.impl

import com.zuraa.blog_api_spring.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

typealias ApplicationUser = com.zuraa.blog_api_spring.entity.User

@Service
class CustomUserServiceDetails(val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails =
        userRepository.findByEmail(username)?.mapToUserDetails()
            ?: throw UsernameNotFoundException("Username not found!")


    private fun ApplicationUser.mapToUserDetails(): UserDetails =
        User.builder()
            .username(this.email)
            .password(this.password)
            .build()
}
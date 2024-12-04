package com.zuraa.blog_api_spring.config

import com.zuraa.blog_api_spring.auth.jwt.JwtAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(val authenticationProvider: AuthenticationProvider) {

    @Bean
    fun securityFilterChain(http: HttpSecurity, jwtAuthFilter: JwtAuthFilter): DefaultSecurityFilterChain {
        // Define public and private routes
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { it.requestMatchers("/login").permitAll() }
            .oauth2Login { oauth2 -> oauth2.defaultSuccessUrl("/home") }
//            .authorizeHttpRequests {
//                it
////                    .requestMatchers("/api/**").authenticated()  //set endpint must be authenticated
//                    .requestMatchers(HttpMethod.POST, "/api/user/register").permitAll()  //set endpoint public
//                    .requestMatchers(HttpMethod.POST, "/api/user/login").permitAll() //set endpoint public
//                    .anyRequest().fullyAuthenticated()
//            }
        // other config
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        // allow localhost for dev purposes
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:3000", "http://localhost:8080")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        configuration.allowedHeaders = listOf("authorization", "content-type")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
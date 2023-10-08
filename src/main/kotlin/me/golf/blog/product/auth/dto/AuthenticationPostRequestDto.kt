package me.golf.blog.product.auth.dto

data class AuthenticationPostRequestDto(
    val email: String,
    val password: String
)

package me.golf.blog.product.auth.dto

data class RefreshPostRequestDto(
    val accessToken: String,
    val refreshToken: String
)
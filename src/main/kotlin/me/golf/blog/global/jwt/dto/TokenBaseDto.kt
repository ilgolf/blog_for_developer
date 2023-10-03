package me.golf.blog.global.jwt.dto

data class TokenBaseDto(
    var accessToken: String,
    var refreshToken: String
)
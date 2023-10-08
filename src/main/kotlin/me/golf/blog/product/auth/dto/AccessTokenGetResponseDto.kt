package me.golf.blog.product.auth.dto

import me.golf.blog.global.jwt.dto.TokenBaseDto

data class AccessTokenGetResponseDto(
    val accessToken: String,
    val refreshToken: String,
) {

    companion object {
        fun of(baseToken: TokenBaseDto): AccessTokenGetResponseDto {
            return AccessTokenGetResponseDto(baseToken.accessToken, baseToken.refreshToken)
        }
    }
}

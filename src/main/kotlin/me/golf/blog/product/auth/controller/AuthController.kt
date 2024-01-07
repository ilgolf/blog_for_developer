package me.golf.blog.product.auth.controller

import jakarta.validation.Valid
import me.golf.blog.product.auth.application.AuthService
import me.golf.blog.product.auth.dto.AccessTokenGetResponseDto
import me.golf.blog.product.auth.dto.AuthenticationPostRequestDto
import me.golf.blog.product.auth.dto.RefreshPostRequestDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auths")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping
    fun auth(@Valid @RequestBody requestDto: AuthenticationPostRequestDto): ResponseEntity<AccessTokenGetResponseDto> {

        return ResponseEntity.ok(AccessTokenGetResponseDto.of(authService.auth(requestDto.email, requestDto.password)))
    }

    @PostMapping("/refresh")
    fun refresh(
        @Valid @RequestBody requestDto: RefreshPostRequestDto
    ): ResponseEntity<AccessTokenGetResponseDto> {

        return ResponseEntity.ok(AccessTokenGetResponseDto.of(authService.refresh(requestDto.accessToken, requestDto.refreshToken)))
    }
}
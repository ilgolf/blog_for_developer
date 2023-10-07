package me.golf.blog.product.board.controller

import jakarta.validation.Valid
import me.golf.blog.global.security.CustomUserDetails
import me.golf.blog.product.board.application.BoardService
import me.golf.blog.product.board.dto.BoardCreatePostRequestDto
import me.golf.blog.product.board.dto.SimpleBoardResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class BoardController(
    private val boardService: BoardService
) {

    @PostMapping
    fun save(
        @Valid @RequestBody requestDto: BoardCreatePostRequestDto,
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ): ResponseEntity<SimpleBoardResponseDto> {

        return ResponseEntity.ok(boardService.save(requestDto.toService(userDetails.memberId)))
    }

//    @PutMapping
//    fun update(
//        @Valid @RequestBody requestDto: BoardUpdatePutRequestDto,
//        @AuthenticationPrincipal userDetails: CustomUserDetails
//    ) {
//
//    }
}
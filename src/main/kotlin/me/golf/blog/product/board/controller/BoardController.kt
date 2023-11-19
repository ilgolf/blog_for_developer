package me.golf.blog.product.board.controller

import jakarta.validation.Valid
import me.golf.blog.global.security.CustomUserDetails
import me.golf.blog.product.board.application.BoardService
import me.golf.blog.product.board.dto.*
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/boards")
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

    @PutMapping("/{boardId}")
    fun update(
        @Valid @RequestBody requestDto: BoardUpdatePutRequestDto,
        @PathVariable boardId: Long,
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ): ResponseEntity<SimpleBoardResponseDto> {

        return ResponseEntity.ok(boardService.update(requestDto.toServiceDto(userDetails.memberId, boardId)))
    }

    @GetMapping
    fun getSummary(
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ): ResponseEntity<List<BoardSummaryGetResponseDto>> {

        return ResponseEntity.ok(boardService.getSummary(userDetails.memberId))
    }

    @GetMapping("/{boardId}")
    fun getDetail(
        @PathVariable boardId: Long,
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ): ResponseEntity<BoardDetailGetResponseDto> {

        return ResponseEntity.ok(boardService.getDetail(userDetails.memberId, boardId))
    }

    @DeleteMapping("/{boardId}")
    fun delete(
        @PathVariable boardId: Long,
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ): ResponseEntity<Unit> {

        boardService.delete(boardId, userDetails.memberId)
        return ResponseEntity.noContent().build()
    }
}
package me.golf.blog.product.post.controller

import jakarta.validation.Valid
import me.golf.blog.global.common.PageCustomResponse
import me.golf.blog.global.security.CustomUserDetails
import me.golf.blog.product.post.application.PostUseCase
import me.golf.blog.product.post.dto.*
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/post")
class PostController(
    private val postUseCase: PostUseCase
) {

    @PostMapping
    fun createTemp(
        @Valid @RequestBody requestDto: PostCreatePostRequestDto,
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ): ResponseEntity<SimplePostResponseDto> {

        return ResponseEntity.ok(postUseCase.createTemp(requestDto.toServiceDto(userDetails.memberId)))
    }

    @PutMapping("/{boardId}/{postId}")
    fun update(
        @Valid @RequestBody requestDto: PostUpdatePutRequestDto,
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable postId: Long,
        @PathVariable boardId: Long
    ): ResponseEntity<SimplePostResponseDto> {

        return ResponseEntity.ok(
            postUseCase.update(
                requestDto.toServiceDto(
                    memberId = userDetails.memberId,
                    boardId = boardId,
                    postId = postId
                )
            )
        )
    }

    @PatchMapping("/{boardId}/{postId}")
    fun publish(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable boardId: Long,
        @PathVariable postId: Long
    ): ResponseEntity<SimplePostResponseDto> {

        return ResponseEntity.ok(postUseCase.publish(postId, boardId, userDetails.memberId))
    }

    @DeleteMapping("/{boardId}/{postId}")
    fun delete(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable boardId: Long,
        @PathVariable postId: Long
    ): ResponseEntity<Unit> {

        postUseCase.delete(PostDeleteRequestDto(postId, boardId, userDetails.memberId))
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{postId}")
    fun getDetails(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable postId: Long
    ): ResponseEntity<PostDetailGetResponseDto> {

        return ResponseEntity.ok(postUseCase.getById(postId, userDetails.memberId))
    }

    @GetMapping("/{boardId}/{categoryId}")
    fun getSummary(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable boardId: Long,
        @PathVariable categoryId: Long,
        @PageableDefault(size = 10) pageable: Pageable
    ): ResponseEntity<PageCustomResponse<PostSummaryGetResponseDto>> {

        return ResponseEntity.ok(
            postUseCase.getSummary(
                PostSummaryRequestDto(
                    boardId = boardId,
                    categoryId = categoryId,
                    memberId = userDetails.memberId,
                    page = pageable.offset,
                    size = pageable.pageSize
                )
            )
        )
    }
}

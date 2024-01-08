package me.golf.blog.product.post.application

import me.golf.blog.global.common.PageCustomResponse
import me.golf.blog.product.post.dto.*
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
interface PostUseCase {
    fun createTemp(requestDto: PostCreateRequestDto): SimplePostResponseDto
    fun update(requestDto: PostUpdateRequestDto): SimplePostResponseDto
    fun publish(postId: Long, boardId: Long, memberId: Long): SimplePostResponseDto
    fun delete(requestDto: PostDeleteRequestDto)
    fun getById(postId: Long, memberId: Long): PostDetailGetResponseDto
    fun getSummary(requestDto: PostSummaryRequestDto): PageCustomResponse<PostSummaryGetResponseDto>
}

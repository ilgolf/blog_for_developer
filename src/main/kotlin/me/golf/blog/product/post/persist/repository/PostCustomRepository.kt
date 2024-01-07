package me.golf.blog.product.post.persist.repository

import me.golf.blog.product.post.dto.PostDetailResponseDto
import me.golf.blog.product.post.dto.PostSummaryGetResponseDto
import me.golf.blog.product.post.dto.PostSummaryRequestDto
import me.golf.blog.product.post.persist.Post
import org.springframework.data.domain.Page

interface PostCustomRepository {
    fun getDetailByIdAndMemberId(postId: Long, memberId: Long): PostDetailResponseDto?

    fun findAllBy(requestDto: PostSummaryRequestDto): Page<PostSummaryGetResponseDto>
}

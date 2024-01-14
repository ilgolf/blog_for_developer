package me.golf.blog.product.post.dto

import java.time.LocalDateTime

data class PostDetailResponseDto(
    val postId: Long,
    val boardId: Long,
    val memberId: Long,
    val title: String,
    val content: String,
    val categoryName: String,
    val boardName: String,
    val memberName: String,
    val createdDate: LocalDateTime,
    val updatedDate: LocalDateTime
)

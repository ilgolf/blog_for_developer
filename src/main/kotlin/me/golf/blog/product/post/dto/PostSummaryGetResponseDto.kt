package me.golf.blog.product.post.dto

import me.golf.blog.product.post.persist.PostSaveStatus
import java.time.LocalDateTime

data class PostSummaryGetResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val likeCount: Long,
    val replyCount: Long,
    val viewCount: Long,
    val postSaveStatus: PostSaveStatus,
    val createdDate: LocalDateTime,
    val lastModifiedDate: LocalDateTime,
    val categoryId: Long,
    val boardId: Long,
    val memberId: Long,
    val categoryName: String,
)
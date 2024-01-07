package me.golf.blog.product.post.dto

import com.querydsl.core.annotations.QueryProjection

data class PostSummaryGetResponseDto

@QueryProjection
constructor(
    val id: Long,
    val title: String,
    val content: String,
    val likeCount: Long,
    val replyCount: Long,
    val viewCount: Long,
    val postSaveStatus: String,
    val createdDate: String,
    val lastModifiedDate: String,
    val categoryId: Long,
    val boardId: Long,
    val memberId: Long,
    val categoryName: String,
)
package me.golf.blog.product.post.dto

data class PostSummaryRequestDto(
    val boardId: Long,
    val categoryId: Long,
    val memberId: Long,
    val page: Long,
    val size: Int
)

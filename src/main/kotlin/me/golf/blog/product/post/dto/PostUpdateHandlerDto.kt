package me.golf.blog.product.post.dto

data class PostUpdateHandlerDto(
    val title: String,
    val content: String,
    val boardId: Long,
    val postId: Long,
    val memberId: Long
)

package me.golf.blog.product.post.dto

data class PostDeleteRequestHandlerDto(
    val postId: Long,
    val boardId: Long,
    val memberId: Long
)

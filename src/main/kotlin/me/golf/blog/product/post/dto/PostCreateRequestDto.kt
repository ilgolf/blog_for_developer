package me.golf.blog.product.post.dto

import me.golf.blog.product.post.persist.Post

data class PostCreateRequestDto(
    val title: String,
    val content: String,
    val categoryId: Long,
    val boardId: Long,
    val memberId: Long
) {

    fun toPost() =
        Post(
            title = this.title,
            content = this.content,
            categoryId = this.categoryId,
            boardId = this.boardId
        )
}

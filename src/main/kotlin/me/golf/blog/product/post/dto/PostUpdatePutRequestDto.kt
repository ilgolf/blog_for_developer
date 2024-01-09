package me.golf.blog.product.post.dto

import jakarta.validation.constraints.NotBlank

data class PostUpdatePutRequestDto(

    @field:NotBlank(message = "제목은 필수 값입니다.")
    val title: String,

    @field:NotBlank(message = "내용은 필수 값입니다.")
    val content: String
) {

    fun toServiceDto(memberId: Long, boardId: Long, postId: Long) = PostUpdateRequestDto(
        title = this.title,
        content = this.content,
        boardId = boardId,
        postId = postId,
        memberId = memberId
    )
}

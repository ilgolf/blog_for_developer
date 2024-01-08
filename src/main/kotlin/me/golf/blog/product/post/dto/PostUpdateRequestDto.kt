package me.golf.blog.product.post.dto

data class PostUpdateRequestDto(
    val title: String,
    val content: String,
    val boardId: Long,
    val postId: Long,
    val memberId: Long
) {

    fun toHandlerDto() = PostUpdateHandlerDto(
        title = this.title,
        content = this.content,
        boardId = this.boardId,
        postId = this.postId,
        memberId = this.memberId
    )
}

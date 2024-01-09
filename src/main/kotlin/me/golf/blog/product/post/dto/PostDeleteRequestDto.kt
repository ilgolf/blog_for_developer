package me.golf.blog.product.post.dto

data class PostDeleteRequestDto(
    val postId: Long,
    val boardId: Long,
    val memberId: Long
) {

    fun toHandlerDto(): PostDeleteRequestHandlerDto {
        return PostDeleteRequestHandlerDto(
            postId = this.postId,
            boardId = this.boardId,
            memberId = this.memberId
        )
    }
}

package me.golf.blog.product.post.dto

data class PostDetailGetResponseDto(
    val postId: Long,
    val boardId: Long,
    val memberId: Long,
    val title: String,
    val content: String,
    val categoryName: String,
    val boardName: String,
    val writer: String,
    val createdDate: String,
    val updatedDate: String
) {
    companion object {
        fun of(postDetailResponseDto: PostDetailResponseDto) =
            PostDetailGetResponseDto(
                postId = postDetailResponseDto.postId,
                boardId = postDetailResponseDto.boardId,
                memberId = postDetailResponseDto.memberId,
                title = postDetailResponseDto.title,
                content = postDetailResponseDto.content,
                categoryName = postDetailResponseDto.categoryName,
                boardName = postDetailResponseDto.boardName,
                writer = postDetailResponseDto.memberName,
                createdDate = postDetailResponseDto.createdDate.toString(),
                updatedDate = postDetailResponseDto.updatedDate.toString()
            )
    }
}

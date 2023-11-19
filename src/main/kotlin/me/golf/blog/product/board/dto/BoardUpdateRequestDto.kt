package me.golf.blog.product.board.dto

data class BoardUpdateRequestDto(
    val title: String,
    val description: String,
    val boardId: Long,
    val memberId: Long
) {
    fun toHandlerDto(): BoardUpdateHandlerRequestDto {
        return BoardUpdateHandlerRequestDto(
            title = this.title,
            description = this.description,
            boardId = this.boardId,
            memberId = this.memberId
        )
    }
}
package me.golf.blog.product.board.dto

data class BoardUpdatePutRequestDto(
    val title: String,
    val description: String
) {

    fun toServiceDto(memberId: Long, boardId: Long): BoardUpdateRequestDto {
        return BoardUpdateRequestDto(
            title = title,
            description = description,
            boardId = boardId,
            memberId = memberId
        )
    }
}

package me.golf.blog.product.board.dto

import me.golf.blog.product.board.persist.Board

data class BoardDetailGetResponseDto(
    val boardId: Long,
    val title: String,
    val description: String,
    val boardUrl: String
) {

    companion object {
        fun convertDetailDto(board: Board): BoardDetailGetResponseDto {

            return BoardDetailGetResponseDto(
                board.id,
                board.title,
                board.description,
                board.boardUrl
            )
        }
    }
}

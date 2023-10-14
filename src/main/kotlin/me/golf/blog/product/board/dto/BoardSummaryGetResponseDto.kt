package me.golf.blog.product.board.dto

import me.golf.blog.product.board.persist.Board

data class BoardSummaryGetResponseDto(
    val boardId: Long,
    val title: String,
    val boardUrl: String
) {

    companion object {
        fun convertResponseDto(board: Board): BoardSummaryGetResponseDto {

            return BoardSummaryGetResponseDto(
                board.id,
                board.title,
                board.boardUrl
            )
        }
    }
}

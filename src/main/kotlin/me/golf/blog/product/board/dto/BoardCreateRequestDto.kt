package me.golf.blog.product.board.dto

import me.golf.blog.product.board.persist.Board

data class BoardCreateRequestDto(
    val title: String,
    val description: String,
    val boardUrl: String,
    val memberId: Long
) {

    fun createBoardEntity(): Board {
        return Board(
            title = this.title,
            description = this.description,
            boardUrl = this.boardUrl,
            memberId = this.memberId
        )
    }
}
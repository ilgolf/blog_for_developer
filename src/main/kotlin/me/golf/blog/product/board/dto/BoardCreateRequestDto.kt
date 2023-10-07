package me.golf.blog.product.board.dto

import me.golf.blog.product.board.persist.Board

data class BoardCreateRequestDto(
    val title: String,
    val description: String,
    val postUrl: String,
    val memberId: Long
) {

    fun createBoardEntity(): Board {
        return Board(
            title = this.title,
            description = this.description,
            postUrl = this.postUrl,
            memberId = this.memberId
        )
    }
}
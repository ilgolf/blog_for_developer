package me.golf.blog.product.board.dto

data class BoardUpdateHandlerRequestDto(
    val title: String,
    val description: String,
    val boardId: Long,
    val memberId: Long
)
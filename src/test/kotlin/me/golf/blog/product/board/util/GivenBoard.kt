package me.golf.blog.product.board.util

import me.golf.blog.product.board.persist.Board

object GivenBoard {

    const val TITLE = "test board title"
    const val DESCRIPTION = "안녕하세요 3년차 개발자 golf 입니다."
    const val BOARD_URL = "https://mock-url/image/3"

    fun createBoardEntity(memberId: Long): Board {
        return Board(
            title = TITLE,
            description = DESCRIPTION,
            boardUrl = BOARD_URL,
            memberId = memberId
        )
    }
}
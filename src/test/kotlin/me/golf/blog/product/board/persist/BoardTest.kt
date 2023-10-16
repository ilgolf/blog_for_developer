package me.golf.blog.product.board.persist

import me.golf.blog.product.board.util.GivenBoard
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class BoardTest {

    private lateinit var board: Board

    @BeforeEach
    fun setup() {
        board = GivenBoard.createBoardEntity(1L)
    }

    @Test
    fun `최초로 board가 생성되면 초기 metrics값은 0이다`() {
        // given
        val newBoard = Board(
            id = 1,
            title = "title",
            description = "description",
            boardUrl = "golf-dev.blog.kr",
            memberId = 1L
        )

        // when
        val boardMetrics = newBoard.boardMetrics

        // then
        assertAll(
            { assertThat(boardMetrics.postCount).isEqualTo(0) },
            { assertThat(boardMetrics.viewCount).isEqualTo(0) }
        )
    }

    @Test
    fun `게시판 정보를 수정한다`() {
        // given
        val updateTitle = "updateTitle"
        val updateDescription = "updateDescription"

        // when
        board.update(updateTitle, updateDescription)

        // then
        assertAll(
            { assertThat(board.title).isEqualTo(updateTitle) },
            { assertThat(board.description).isEqualTo(updateDescription) },
        )
    }
}
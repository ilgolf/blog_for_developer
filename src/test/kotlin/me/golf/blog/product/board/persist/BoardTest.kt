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
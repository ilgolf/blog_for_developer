package me.golf.blog.product.board.application

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.golf.blog.product.board.dto.BoardCreateRequestDto
import me.golf.blog.product.board.dto.BoardUpdateRequestDto
import me.golf.blog.product.board.handler.BoardRepositoryHandler
import me.golf.blog.product.board.persist.Board
import me.golf.blog.product.board.util.GivenBoard
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BoardServiceTest {

    private val boardRepositoryHandler = mockk<BoardRepositoryHandler>()

    private lateinit var boardService: BoardService
    private lateinit var board: Board

    @BeforeEach
    fun setUp() {
        boardService = BoardService(boardRepositoryHandler)
        board = GivenBoard.createBoardEntity(memberId = 1L)
    }

    @Test
    fun `게시판을 저장하는데 성공한다`() {
        // given
        val requestDto = BoardCreateRequestDto(
            title = GivenBoard.TITLE,
            description = GivenBoard.DESCRIPTION,
            boardUrl = GivenBoard.BOARD_URL,
            memberId = 1L
        )

        every { boardRepositoryHandler.save(any()) } returns board

        // when
        boardService.save(requestDto)

        // then
        verify(exactly = 1) { boardRepositoryHandler.save(any()) }
    }

    @Test
    fun `게시판 설정을 수정하고 업데이트 한다`() {
        // given
        val requestDto = BoardUpdateRequestDto(
            title = GivenBoard.TITLE,
            description = GivenBoard.DESCRIPTION,
            boardId = 1L,
            memberId = 1L
        )

        every { boardRepositoryHandler.update(any()) } returns board

        // when
        boardService.update(requestDto)

        // then
        verify(exactly = 1) { boardRepositoryHandler.update(any()) }
    }

    @Test
    fun `회원이 갖고 있는 요약된 게시판 정보를 모두 보여준다`() {
        // given
        val boards = mutableListOf(board)

        for (i: Int in 1 .. 2) {
            boards.add(
                Board(
                    title = GivenBoard.TITLE + "$i",
                    description = GivenBoard.DESCRIPTION,
                    boardUrl = "https://mock-url$i/image/$i",
                    memberId = 1L
                )
            )
        }

        every { boardRepositoryHandler.getBoards(any()) } returns boards

        // when
        val responseDtos = boardService.getSummary(memberId = 1L)

        // then
        verify(exactly = 1) { boardRepositoryHandler.getBoards(any()) }
        assertThat(responseDtos.size).isEqualTo(3)
    }

    @Test
    fun `게시판 상세 정보를 조회한다`() {
        // given
        every { boardRepositoryHandler.getBoard(any(), any()) } returns board

        // when
        boardService.getDetail(1L, 1L)

        // then
        verify { boardRepositoryHandler.getBoard(any(), any()) }
    }
}
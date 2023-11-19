package me.golf.blog.product.board.handler

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.golf.blog.product.board.dto.BoardUpdateHandlerRequestDto
import me.golf.blog.product.board.exception.BoardException
import me.golf.blog.product.board.persist.Board
import me.golf.blog.product.board.persist.BoardRepository
import me.golf.blog.product.board.util.GivenBoard
import me.golf.blog.product.member.exception.MemberException
import me.golf.blog.product.member.handler.MemberRepositoryHandler
import me.golf.blog.product.member.persist.Member
import me.golf.blog.product.member.util.GivenMember
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class BoardRepositoryHandlerTest {

    private val boardRepository = mockk<BoardRepository>()
    private val memberRepositoryHandler = mockk<MemberRepositoryHandler>()

    private lateinit var boardRepositoryHandler: BoardRepositoryHandler
    private lateinit var board: Board
    private lateinit var member: Member

    @BeforeEach
    fun setUp() {
        boardRepositoryHandler = BoardRepositoryHandler(boardRepository, memberRepositoryHandler)
        board = GivenBoard.createBoardEntity(memberId = 1L)
        member = GivenMember.toMember()
        member.id = 1L
        board.id = 1L
    }

    @Test
    fun `게시판을 DB에 저장하는데 성공한다`() {
        // given
        every { boardRepository.existsByTitle(any()) } returns false
        every { boardRepository.existsByBoardUrl(any()) } returns false
        every { boardRepository.countByMemberId(any()) } returns 3
        every { memberRepositoryHandler.getMember(any()) } returns member
        every { boardRepository.save(any()) } returns board

        // when
        boardRepositoryHandler.save(board)

        // then
        verify(exactly = 1) { boardRepository.save(any()) }
    }

    @Test
    fun `게시판을 저장 시 이미 제목 또는 url이 존재하면 예외가 발생한다`() {
        // given
        every { boardRepository.existsByBoardUrl(any()) } returns true
        every { boardRepository.countByMemberId(any()) } returns 3
        every { memberRepositoryHandler.getMember(any()) } returns member

        // when
        val exception = catchException { boardRepositoryHandler.save(board) }

        // then
        verify(exactly = 0) { boardRepository.save(any()) }
        assertAll(
            { assertThat(exception).isInstanceOf(BoardException.AlreadyExistException::class.java) },
            { assertThat(exception.message).isEqualTo("해당 내용의 게시판이 이미 존재합니다.: ${board.boardUrl}") }
        )
    }

    @Test
    fun `존재하지 않는 회원으로 게시판 저장을 시도하면 예외가 발생한다`() {
        // given
        every { memberRepositoryHandler.getMember(any()) } throws MemberException.MemberNotFoundException("1")

        // when
        val exception = catchException { boardRepositoryHandler.save(board) }

        // then
        verify(exactly = 0) { boardRepository.save(any()) }
        assertAll(
            { assertThat(exception).isInstanceOf(MemberException.MemberNotFoundException::class.java) },
            { assertThat(exception.message).isEqualTo("회원을 찾지 못하였습니다.: ${member.id}") }
        )
    }

    @Test
    fun `게시판 개수가 4개 이상이 되면 더 이상 게시판을 만들 수 없다`() {
        // given
        every { boardRepository.existsByTitle(any()) } returns false
        every { boardRepository.existsByBoardUrl(any()) } returns false
        every { boardRepository.countByMemberId(any()) } returns 4
        every { memberRepositoryHandler.getMember(any()) } returns member

        // when
        val exception = catchException { boardRepositoryHandler.save(board) }

        // then
        verify(exactly = 0) { boardRepository.save(any()) }
        assertAll(
            { assertThat(exception).isInstanceOf(BoardException.BoardLimitExceededException::class.java) },
            { assertThat(exception.message).isEqualTo("게시판 보유 횟수인 4개를 초과했습니다: ") }
        )
    }

    @Test
    fun `게시판 정보를 DB에 업데이트 하기전 사전 검증 이후 가져온다`() {
        // given
        val requestDto = BoardUpdateHandlerRequestDto(
            title  = "new Title",
            description = "description new",
            boardId = 1L,
            memberId = 1L
        )

        every { boardRepository.existsByTitle(any()) } returns false
        every { boardRepository.findByIdAndMemberId(any(), any()) } returns board

        // when
        boardRepositoryHandler.update(requestDto)

        // then
        verify(exactly = 1) { boardRepository.findByIdAndMemberId(any(), any()) }
    }

    @Test
    fun `이미 존재하는 제목으로 수정을 시도하면 예외가 발생한다`() {
        // given
        val requestDto = BoardUpdateHandlerRequestDto(
            title  = "new Title",
            description = "new description",
            boardId = 1L,
            memberId = 1L
        )

        every { boardRepository.existsByTitle(any()) } returns true
        every { boardRepository.findByIdAndMemberId(any(), any()) } returns board

        // when
        val exception = catchException { boardRepositoryHandler.update(requestDto) }

        // then
        verify(exactly = 0) { boardRepository.findByIdAndMemberId(any(), any()) }
        assertAll(
            { assertThat(exception).isInstanceOf(BoardException.AlreadyExistException::class.java) },
            { assertThat(exception.message).isEqualTo("해당 내용의 게시판이 이미 존재합니다.: ${requestDto.title}") }
        )
    }

    @Test
    fun `이미 있는 게시판 제목으로 업데이트를 시도하면 예외가 발생한다`() {
        // given
        val requestDto = BoardUpdateHandlerRequestDto(
            title  = "new Title",
            description = "new description",
            boardId = 1L,
            memberId = 1L
        )

        every { boardRepository.existsByTitle(any()) } returns false
        every { boardRepository.findByIdAndMemberId(any(), any()) } returns board

        // when
        boardRepositoryHandler.update(requestDto)

        // then
        verify(exactly = 1) { boardRepository.findByIdAndMemberId(any(), any()) }
    }

    @Test
    fun `게시판 요약 리스트로 만들기 위한 게시판 정보를 가져온다`() {
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


        every { boardRepository.findByMemberIdOrderByIdDesc(any()) } returns boards

        // when
        boardRepositoryHandler.getBoards(1L)

        // then
        verify(exactly = 1) { boardRepository.findByMemberIdOrderByIdDesc(any()) }
    }

    @Test
    fun `상세 데이터를 만들기 위한 게시판 정보를 받아온다`() {
        // given
        every { boardRepository.findByIdAndMemberId(any(), any()) } returns board

        // when
        boardRepositoryHandler.getBoard(1L, 1L)

        // then
        verify { boardRepository.findByIdAndMemberId(any(), any()) }
    }

    @Test
    fun `상세 데이터를 못가져오면 예외 발생한다`() {
        // given
        every { boardRepository.findByIdAndMemberId(any(), any()) } returns null

        // when
        val exception = catchException { boardRepositoryHandler.getBoard(1L, 1L) }

        // then
        assertAll(
            { assertThat(exception).isInstanceOf(BoardException.NotFoundException::class.java) },
            { assertThat(exception.message).isEqualTo("게시판이 존재하지 않습니다.: 1") }
        )
    }
}
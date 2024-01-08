package me.golf.blog.product.board.handler

import me.golf.blog.product.board.dto.BoardUpdateHandlerRequestDto
import me.golf.blog.product.board.exception.BoardException
import me.golf.blog.product.board.persist.Board
import me.golf.blog.product.board.persist.BoardRepository
import me.golf.blog.product.member.handler.MemberRepositoryHandler
import org.springframework.stereotype.Component

@Component
class BoardRepositoryHandler(
    private val boardRepository: BoardRepository,
    private val memberRepositoryHandler: MemberRepositoryHandler
) {

    companion object {
        const val BOARD_COUNT_LIMIT = 4
    }

    fun save(board: Board): Board {

        memberRepositoryHandler.getMember(board.memberId).memberMetrics.plusBoardCount()

        validationExistBeforeSave(board.boardUrl, board.title)

        if (boardRepository.countByMemberId(board.memberId) >= BOARD_COUNT_LIMIT) {
            throw BoardException.BoardLimitExceededException()
        }

        return boardRepository.save(board)
    }

    fun update(requestDto: BoardUpdateHandlerRequestDto): Board {

        validateIfExists(boardRepository.existsByTitle(requestDto.title), requestDto.title)

        return getBoard(requestDto.memberId, requestDto.boardId)
            .also { it.update(requestDto.title, requestDto.description) }
    }

    fun getBoards(memberId: Long): List<Board> {

        return boardRepository.findByMemberIdOrderByIdDesc(memberId)
    }

    fun getBoard(memberId: Long, boardId: Long): Board {

        return boardRepository.findByIdAndMemberId(boardId, memberId)
            ?: throw BoardException.NotFoundException(boardId)
    }

    fun delete(boardId: Long, memberId: Long) {

        getBoard(memberId, boardId).delete()
    }

    fun validationExistBoardByIdAndMemberId(boardId: Long, memberId: Long): Boolean {

        return boardRepository.existsByIdAndMemberId(boardId, memberId)
    }

    private fun validationExistBeforeSave(postUrl: String, title: String) {

        validateIfExists(boardRepository.existsByBoardUrl(postUrl), postUrl)
        validateIfExists(boardRepository.existsByTitle(title), title)
    }

    private fun validateIfExists(exists: Boolean, value: String) {

        if (exists) {
            throw BoardException.AlreadyExistException(value)
        }
    }
}

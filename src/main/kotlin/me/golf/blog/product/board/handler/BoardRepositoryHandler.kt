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

    fun beforeSaveProcess(board: Board): Board {

        memberRepositoryHandler.getMember(board.memberId).memberMetrics.plusBoardCount()

        validationExistBeforeSave(board.boardUrl, board.title)

        if (boardRepository.countByMemberId(board.memberId) >= 4) {
            throw BoardException.BoardLimitExceededException()
        }

        return boardRepository.save(board)
    }

    fun beforeUpdateProcess(requestDto: BoardUpdateHandlerRequestDto): Board {

        validateIfExists(boardRepository.existsByTitle(requestDto.title), requestDto.title)

        return getBoard(requestDto.memberId, requestDto.boardId)
    }

    fun beforeGetSummaryProcess(memberId: Long): List<Board> {

        return boardRepository.findByMemberIdOrderByIdDesc(memberId)
    }

    fun beforeGetDetailProcess(memberId: Long, boardId: Long): Board {

        return getBoard(memberId, boardId)
    }

    private fun getBoard(memberId: Long, boardId: Long): Board {

        return boardRepository.findByIdAndMemberId(boardId, memberId)
            ?: throw BoardException.NotFoundException(boardId)
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

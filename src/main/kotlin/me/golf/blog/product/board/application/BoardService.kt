package me.golf.blog.product.board.application

import me.golf.blog.product.board.dto.*
import me.golf.blog.product.board.handler.BoardRepositoryHandler
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BoardService(
    private val boardRepositoryHandler: BoardRepositoryHandler
) {

    @Transactional
    fun save(requestDto: BoardCreateRequestDto): SimpleBoardResponseDto {

        val board = boardRepositoryHandler.beforeSaveProcess(requestDto.createBoardEntity())
        return SimpleBoardResponseDto(boardId = board.id, boardUrl = board.boardUrl)
    }

    @Transactional
    fun update(requestDto: BoardUpdateRequestDto): SimpleBoardResponseDto {

        val board = boardRepositoryHandler.beforeUpdateProcess(requestDto.toHandlerDto())
            .also { it.update(requestDto.title, requestDto.description) }

        return SimpleBoardResponseDto(boardId = board.id, boardUrl = board.boardUrl)
    }

    @Transactional(readOnly = true)
    fun getSummary(memberId: Long): List<BoardSummaryGetResponseDto> {

        val boards = boardRepositoryHandler.beforeGetSummaryProcess(memberId)
        return boards.map { BoardSummaryGetResponseDto.convertResponseDto(it) }
    }

    @Transactional(readOnly = true)
    fun getDetail(memberId: Long, boardId: Long): BoardDetailGetResponseDto {

        val board = boardRepositoryHandler.beforeGetDetailProcess(memberId, boardId)
        return BoardDetailGetResponseDto.convertDetailDto(board)
    }
}

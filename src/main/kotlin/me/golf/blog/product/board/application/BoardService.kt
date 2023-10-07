package me.golf.blog.product.board.application

import me.golf.blog.product.board.dto.BoardCreateRequestDto
import me.golf.blog.product.board.dto.SimpleBoardResponseDto
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
        return SimpleBoardResponseDto(boardId = board.id, postUrl = board.postUrl)
    }
}

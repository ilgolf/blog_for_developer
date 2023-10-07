package me.golf.blog.product.board.handler

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
        validationExistBeforeSave(board.postUrl, board.title, board.memberId)
        return boardRepository.save(board)
    }

    private fun validationExistBeforeSave(postUrl: String, title: String, memberId: Long) {
        validateIfExists(memberRepositoryHandler.validateMemberExist(memberId), memberId.toString())
        validateIfExists(boardRepository.existsByPostUrl(postUrl), postUrl)
        validateIfExists(boardRepository.existsByTitle(title), title)
    }

    private fun validateIfExists(exists: Boolean, value: String) {
        if (exists) {
            throw BoardException.AlreadyExistException(value)
        }
    }
}

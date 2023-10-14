package me.golf.blog.product.board.exception

import me.golf.blog.global.exception.error.BusinessException
import me.golf.blog.global.exception.error.ErrorCode

sealed class BoardException {
    class AlreadyExistException(value: String) : BusinessException(ErrorCode.BOARD_ALREADY_EXIST, value)
    class NotFoundException(value: Long) : BusinessException(ErrorCode.BOARD_NOT_FOUND, value.toString())
    class BoardLimitExceededException : BusinessException(ErrorCode.BOARD_EXCEED_REACH)
}
